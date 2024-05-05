package io.github.frpdoliv3.bytebin.repository

import io.github.frpdoliv3.bytebin.util.compareTo
import io.github.frpdoliv3.bytebin.util.mib
import io.github.frpdoliv3.bytebin.util.plus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Repository
import redis.clients.jedis.commands.JedisBinaryCommands
import redis.clients.jedis.params.SetParams

@Repository
class JedisChunkCacheRepository(
    private val commands: JedisBinaryCommands,
): ChunkCacheRepository {
    private fun putContentIfNotExists(chunkId: Int, content: ByteArray): Boolean {
        val setParams = SetParams().apply {
            nx()
            ex(120)
        }
        val response = commands.set(chunkId.toByteArray(), content, setParams)
        return response == "OK"
    }

    override fun putContent(chunkId: Int, content: ByteArray): Long {
        if (!putContentIfNotExists(chunkId, content)) {
            commands.append(chunkId.toByteArray(), content)
        }
        return commands.strlen(chunkId.toByteArray())
    }

    fun splitChunk(size: Long) = sequence<Long> {
        if (size <= THRESHOLD) {
            yield(size)
        } else {
            var sentData = 0L
            while(sentData < size) {
                if ((sentData + THRESHOLD) > size) {
                    yield(size - sentData)
                } else {
                    yield(THRESHOLD.absoluteSize())
                }
                sentData += THRESHOLD
            }
        }
    }

    override suspend fun getContentSize(chunkId: Int) = withContext(Dispatchers.IO) {
        commands.strlen(chunkId.toByteArray())
    }

    override fun getContent(chunkId: Int) = flow {
        val chunkTotalSize = getContentSize(chunkId)
        var startOffset = 0L
        for (chunkSize in splitChunk(chunkTotalSize)) {
            val endOffset = startOffset + chunkSize - 1
            emit(getStringSlice(chunkId, startOffset, endOffset))
            startOffset = endOffset + 1
        }
    }

    override fun deleteContent(chunkId: Int) {
        commands.del(chunkId.toByteArray())
    }

    suspend fun getStringSlice(chunkId: Int, startOffset: Long, endOffset: Long): ByteArray = withContext(Dispatchers.IO) {
        commands.getrange(chunkId.toByteArray(), startOffset, endOffset)
    }

    companion object {
        val THRESHOLD = 50.mib
    }
}

fun Int.toByteArray(): ByteArray = this.toString().encodeToByteArray()
fun ByteArray.toInt(): Int = this.toString(Charsets.UTF_8).toInt()
