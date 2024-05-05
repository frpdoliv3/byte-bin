package io.github.frpdoliv3.bytebin.model

sealed class Chunk {
    data class Details(
        val id: Int,
        val position: Int,
        val startByte: Long,
        val endByte: Long,
        val length: Long,
        val status: ChunkStatus,
        val fileId: String
    ): Chunk()
}