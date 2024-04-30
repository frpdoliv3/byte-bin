package io.github.frpdoliv3.bytebin.repository

import kotlinx.coroutines.flow.Flow

interface ChunkContentRepository {
    fun putContent(chunkId: Int, content: ByteArray): Long
    fun getContent(chunkId: Int): Flow<ByteArray>
    suspend fun getContentSize(chunkId: Int): Long
    fun deleteContent(chunkId: Int)
}