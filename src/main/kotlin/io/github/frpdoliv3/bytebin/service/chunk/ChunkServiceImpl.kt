package io.github.frpdoliv3.bytebin.service.chunk

import io.github.frpdoliv3.bytebin.entity.Chunk
import io.github.frpdoliv3.bytebin.entity.File
import io.github.frpdoliv3.bytebin.model.ChunkData
import io.github.frpdoliv3.bytebin.model.ChunkStatus
import io.github.frpdoliv3.bytebin.repository.ChunkCacheRepository
import io.github.frpdoliv3.bytebin.repository.ChunkRepository
import io.github.frpdoliv3.bytebin.util.compareTo
import io.github.frpdoliv3.bytebin.util.div
import io.github.frpdoliv3.bytebin.util.mib
import io.github.frpdoliv3.bytebin.util.rem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import io.github.frpdoliv3.bytebin.model.Chunk as ChunkModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ChunkServiceImpl(
    private val chunkRepo: ChunkRepository,
    private val chunkCacheRepo: ChunkCacheRepository
): ChunkService {
    private val _events: MutableSharedFlow<ChunkService.Event> = MutableSharedFlow()
    override val events: Flow<ChunkService.Event> = _events.asSharedFlow()

    private fun chunkSplits(fileSize: Long): Sequence<Long> = sequence {
        if (fileSize < 8.mib) {
            yield(fileSize)
        } else if (fileSize <= 400.mib) {
            var remaining = fileSize
            val split = fileSize / 4
            for (i in 0 until 3) {
                yield(split)
                remaining -= split
            }
            yield(remaining)
        } else {
            val splits = fileSize / 100.mib
            val remainingBytes = fileSize % 100.mib
            for (i in 0 until splits - 1) {
                yield(100.mib.absoluteSize())
            }
            if (remainingBytes > 8.mib) {
                yield(100.mib.absoluteSize())
                for (subChunk in chunkSplits(remainingBytes)) {
                    yield(subChunk)
                }
            } else {
                yield(100.mib.absoluteSize() + remainingBytes)
            }
        }
    }

    override suspend fun createChunks(file: File, fileSize: Long): List<ChunkModel.Details> {
        var position = 1
        var startingByte = 0L
        val createdChunks = mutableListOf<Chunk>()
        for (chunkSize in chunkSplits(fileSize)) {
            val endByte = startingByte + chunkSize
            val chunk = Chunk(
                position = position,
                startByte = startingByte,
                endByte = endByte - 1,
                length = chunkSize,
                file = file
            )
            createdChunks.add(withContext(Dispatchers.IO) {
                chunkRepo.save(chunk)
            })
            startingByte = endByte
            position += 1
        }
        return createdChunks.map { it.toModel() }
    }

    private fun findUploadingChunk(fileId: String): Chunk? {
        val uploadingChunk = chunkRepo.findChunkByStatusAndFileIdOrderByPosition(ChunkStatus.UPLOADING, fileId)
        if (uploadingChunk.isPresent) {
            return uploadingChunk.get()
        }
        val pendingChunk = chunkRepo.findChunkByStatusAndFileIdOrderByPosition(ChunkStatus.PENDING, fileId).getOrNull()
            ?: return null
        pendingChunk.status = ChunkStatus.UPLOADING
        return chunkRepo.save(pendingChunk)
    }

    override suspend fun loadChunkData(chunkId: Int): ChunkData = ChunkData(
        payload = chunkCacheRepo.getContent(chunkId),
        contentLength = chunkCacheRepo.getContentSize(chunkId)
    )

    override fun findFileByChunkId(chunkId: Int): File? {
        val chunk = chunkRepo.findById(chunkId).getOrNull() ?: return null
        return chunk.file
    }

    override suspend fun uploadChunkPayload(fileId: String, payload: ByteArray) {
        val uploadingChunk = findUploadingChunk(fileId) ?: return
        val cachedSize = chunkCacheRepo.putContent(uploadingChunk.id!!, payload)
        if (cachedSize == uploadingChunk.length) {
            _events.emit(ChunkService.Event.ChunkUploaded(uploadingChunk.id!!))
        }
        uploadingChunk.status = ChunkStatus.UPLOADED
        withContext(Dispatchers.IO) {
            chunkRepo.save(uploadingChunk)
        }
    }
}
