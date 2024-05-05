package io.github.frpdoliv3.bytebin.service.file

import io.github.frpdoliv3.bytebin.controller.file.dto.CreateFileRequest
import io.github.frpdoliv3.bytebin.entity.File

interface FileService {
    suspend fun createFile(request: CreateFileRequest): String?
    fun fileStatus(fileId: String): File.Status?
    fun startMultipartUpload(fileId: String)
    fun startUpload(fileId: String)
    fun isMultipart(fileId: String): Boolean?
    fun upload(fileId: String, payload: ByteArray)
}