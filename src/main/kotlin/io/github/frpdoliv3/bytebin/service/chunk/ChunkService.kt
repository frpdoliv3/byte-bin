package io.github.frpdoliv3.bytebin.service.chunk

import io.github.frpdoliv3.bytebin.entity.File

interface ChunkService {
    fun createChunks(file: File, fileSize: Long)
}