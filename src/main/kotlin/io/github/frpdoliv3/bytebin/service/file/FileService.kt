package io.github.frpdoliv3.bytebin.service.file

import arrow.core.Either
import io.github.frpdoliv3.bytebin.handler.file.dto.CreateFileRequest
import io.github.frpdoliv3.bytebin.entity.File
import io.github.frpdoliv3.bytebin.model.File as FileModel
import io.github.frpdoliv3.bytebin.model.Chunk

interface FileService {
    suspend fun createFile(request: CreateFileRequest): Outcome<FileModel>
    fun fileStatus(fileId: String): File.Status?
    fun startMultipartUpload(fileId: String): Outcome<Unit>
    fun startUpload(fileId: String): Either<Error, Unit>
    fun isMultipart(fileId: String): Boolean?
    fun upload(fileId: String, payload: ByteArray)

    sealed class Error {
        data object FileNotFound: Outcome()
        data object InternalError: Outcome()
        data object AlreadyUploading: Outcome()
    }
}
