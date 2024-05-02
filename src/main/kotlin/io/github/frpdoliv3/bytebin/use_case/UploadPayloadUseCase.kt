package io.github.frpdoliv3.bytebin.use_case

import io.github.frpdoliv3.bytebin.service.file.FileService
import org.springframework.stereotype.Component

//TODO: analyse if it's the best annotation available
@Component
class UploadPayloadUseCase(
    private val fileService: FileService
) {

    operator fun invoke(fileId: String, payload: ByteArray) {

    }
}