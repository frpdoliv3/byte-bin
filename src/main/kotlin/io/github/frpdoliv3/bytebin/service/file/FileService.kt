package io.github.frpdoliv3.bytebin.service.file

import io.github.frpdoliv3.bytebin.handler.file.dto.CreateFileRequest
import io.github.frpdoliv3.bytebin.entity.File
import io.github.frpdoliv3.bytebin.handler.file.dto.CreateFileResponse
import io.github.frpdoliv3.bytebin.model.Chunk

interface FileService {
    suspend fun createFile(request: CreateFileRequest): CreateFileResult
    fun fileStatus(fileId: String): File.Status?
    fun startMultipartUpload(fileId: String)
    fun startUpload(fileId: String)
    fun isMultipart(fileId: String): Boolean?
    fun upload(fileId: String, payload: ByteArray)
}

sealed class CreateFileResult {
    data class Success(
        val fileId: String,
        val chunks: List<Chunk.Details>
    ): CreateFileResult()
    data object Error: CreateFileResult()
}
