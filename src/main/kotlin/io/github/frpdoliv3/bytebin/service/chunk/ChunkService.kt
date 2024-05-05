package io.github.frpdoliv3.bytebin.service.chunk

import io.github.frpdoliv3.bytebin.entity.File
import io.github.frpdoliv3.bytebin.model.ChunkData
import kotlinx.coroutines.flow.Flow

interface ChunkService {
    val events: Flow<Event>

    fun createChunks(file: File, fileSize: Long)
    suspend fun uploadChunkPayload(fileId: String, payload: ByteArray)
    fun findFileByChunkId(chunkId: Int): File?
    suspend fun loadChunkData(chunkId: Int): ChunkData

    sealed class Event {
        data class ChunkUploaded(val chunkId: Int): Event()
    }
}
