package io.github.frpdoliv3.bytebin.repository

import kotlinx.coroutines.flow.Flow

interface FileStorageRepository {
    suspend fun startMultipartUpload(fileId: String): String
    suspend fun uploadFile(fileId: String, payload: ByteArray): String?
    suspend fun uploadFile(fileId: String, payload: Flow<ByteArray>, payloadLength: Long): String?
}