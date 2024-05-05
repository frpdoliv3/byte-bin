package io.github.frpdoliv3.bytebin.repository

import io.github.frpdoliv3.bytebin.entity.Chunk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ChunkRepository: JpaRepository<Chunk, Int> {
    fun findChunkByStatusAndFileIdOrderByPosition(status: Chunk.Status, fileId: String): Optional<Chunk>
}
