package io.github.frpdoliv3.bytebin.handler.file

import io.github.frpdoliv3.bytebin.handler.file.dto.CreateFileRequest
import io.github.frpdoliv3.bytebin.handler.file.dto.toCreateFileResponse
import io.github.frpdoliv3.bytebin.service.file.CreateFileResult
import io.github.frpdoliv3.bytebin.service.file.FileService
import io.github.frpdoliv3.bytebin.use_case.upload_payload.UploadPayloadUseCase
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class FileHandler(
    private val fileService: FileService,
    private val uploadPayloadUseCase: UploadPayloadUseCase
) {
    suspend fun createFile(request: ServerRequest): ServerResponse {
        val requestBody = request.awaitBody<CreateFileRequest>()
        return when(val createdFileResponse = fileService.createFile(requestBody)) {
            is CreateFileResult.Success -> ServerResponse
                .status(HttpStatus.CREATED)
                .bodyValue(createdFileResponse.toCreateFileResponse())
                .awaitSingle()
            is CreateFileResult.Error -> ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .buildAndAwait()
        }
    }

    suspend fun uploadFile(request: ServerRequest): ServerResponse {
        val fileId = request.pathVariable("file_id")
        val payload = request.awaitBody<ByteArray>()
        uploadPayloadUseCase(fileId, payload)
        return ServerResponse.status(501).buildAndAwait()
    }
}
