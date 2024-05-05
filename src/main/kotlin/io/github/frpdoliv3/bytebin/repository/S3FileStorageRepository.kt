package io.github.frpdoliv3.bytebin.repository

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.CreateMultipartUploadRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.toByteStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.stereotype.Repository

@Repository
class S3FileStorageRepository(
    private val s3Client: S3Client
): FileStorageRepository {
    private val bucket = "byte-bin-files"

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override suspend fun startMultipartUpload(fileId: String): String {
        val createMultipartUploadRequest = CreateMultipartUploadRequest {
            bucket = this@S3FileStorageRepository.bucket
            key = fileId
        }
        val result = s3Client.createMultipartUpload(createMultipartUploadRequest)
        return result.uploadId!!
    }

    override suspend fun uploadFile(fileId: String, payload: ByteArray): String? {
        return uploadFile(fileId, flow { emit(payload) }, payload.size.toLong())
    }

    override suspend fun uploadFile(fileId: String, payload: Flow<ByteArray>, payloadLength: Long): String? {
        val putObjectRequest = PutObjectRequest {
            bucket = this@S3FileStorageRepository.bucket
            key = fileId
            body = payload.toByteStream(coroutineScope, contentLength = payloadLength)
        }
        val response = s3Client.putObject(putObjectRequest)
        return response.eTag
    }
}
