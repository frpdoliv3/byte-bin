package io.github.frpdoliv3.bytebin.use_case.upload_payload

import io.github.frpdoliv3.bytebin.entity.File
import io.github.frpdoliv3.bytebin.service.file.FileService
import org.springframework.stereotype.Component

//TODO: analyse if it's the best annotation available
@Component
class UploadPayloadUseCase(
    private val fileService: FileService
) {
    private fun handleUploadStart(fileId: String, isMultipart: Boolean) {
        if (isMultipart) {
            fileService.startMultipartUpload(fileId)
        } else {
            fileService.startUpload(fileId)
        }
    }

    operator fun invoke(fileId: String, payload: ByteArray): UploadPayloadResult {
        val fileStatus = fileService.fileStatus(fileId) ?: return UploadPayloadResult.FileNotFound
        if (fileStatus == File.Status.DONE) {
            return UploadPayloadResult.FileAlreadyUploaded
        }
        val isFileMultipart = fileService.isMultipart(fileId)!!
        if (fileStatus == File.Status.PENDING) {
            handleUploadStart(fileId, isFileMultipart)
        }
        fileService.upload(fileId, payload)
        return UploadPayloadResult.Success
    }
}