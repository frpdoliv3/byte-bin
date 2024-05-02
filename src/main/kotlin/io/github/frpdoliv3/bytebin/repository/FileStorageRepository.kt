package io.github.frpdoliv3.bytebin.repository

interface FileStorageRepository {
    suspend fun startMultipartUpload(fileId: String): String
}