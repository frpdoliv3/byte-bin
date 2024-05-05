package io.github.frpdoliv3.bytebin.model

import kotlinx.coroutines.flow.Flow

data class ChunkData(
    val payload: Flow<ByteArray>,
    val contentLength: Long
)
