package io.github.frpdoliv3.bytebin.service.file

import io.github.frpdoliv3.bytebin.controller.file.dto.CreateFileRequest
import io.github.frpdoliv3.bytebin.entity.File

interface FileService {
    fun createFile(request: CreateFileRequest): Boolean
    fun fileStatus(fileId: String): File.Status?
    fun startMultipartUpload(fileId: String)
    fun startUpload(fileId: String)
    fun isMultipart(fileId: String): Boolean?
    fun upload(fileId: String, payload: ByteArray)
}