package io.github.frpdoliv3.bytebin.repository

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.CreateMultipartUploadRequest
import org.springframework.stereotype.Repository

@Repository
class S3FileStorageRepository(
    private val s3Client: S3Client
): FileStorageRepository {
    override suspend fun startMultipartUpload(fileId: String): String {
        val createMultipartUploadRequest = CreateMultipartUploadRequest {
            bucket = "byte-bin-files"
            key = fileId
        }
        val result = s3Client.createMultipartUpload(createMultipartUploadRequest)
        return result.uploadId!!
    }
}
