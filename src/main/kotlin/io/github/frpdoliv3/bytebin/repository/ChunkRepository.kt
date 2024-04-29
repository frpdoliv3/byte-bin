package io.github.frpdoliv3.bytebin.repository

import io.github.frpdoliv3.bytebin.entity.Chunk
import org.springframework.data.jpa.repository.JpaRepository

interface ChunkRepository: JpaRepository<Chunk, Int>
