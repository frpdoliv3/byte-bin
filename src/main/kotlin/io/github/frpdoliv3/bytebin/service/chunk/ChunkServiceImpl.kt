package io.github.frpdoliv3.bytebin.service.chunk

import io.github.frpdoliv3.bytebin.entity.Chunk
import io.github.frpdoliv3.bytebin.entity.File
import io.github.frpdoliv3.bytebin.repository.ChunkRepository
import io.github.frpdoliv3.bytebin.util.compareTo
import io.github.frpdoliv3.bytebin.util.div
import io.github.frpdoliv3.bytebin.util.mib
import io.github.frpdoliv3.bytebin.util.rem
import org.springframework.stereotype.Service

@Service
class ChunkServiceImpl(
    private val chunkRepo: ChunkRepository,
): ChunkService {
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

    override fun createChunks(file: File, fileSize: Long) {
        var position = 1
        var startingByte = 0L
        for (chunkSize in chunkSplits(fileSize)) {
            val endByte = startingByte + chunkSize
            val chunk = Chunk(
                position = position,
                startByte = startingByte,
                endByte = endByte - 1,
                length = chunkSize,
                file = file
            )
            chunkRepo.save(chunk)
            startingByte = endByte
            position += 1
        }
    }
}
