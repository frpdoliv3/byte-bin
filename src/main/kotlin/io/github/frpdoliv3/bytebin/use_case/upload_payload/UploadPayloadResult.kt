package io.github.frpdoliv3.bytebin.use_case.upload_payload

sealed class UploadPayloadResult{
    data object FileAlreadyUploaded: UploadPayloadResult()
    data object FileNotFound: UploadPayloadResult()
    data object Success: UploadPayloadResult()
}
