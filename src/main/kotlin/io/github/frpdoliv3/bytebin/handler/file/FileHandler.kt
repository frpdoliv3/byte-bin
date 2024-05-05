package io.github.frpdoliv3.bytebin.handler.file

import io.github.frpdoliv3.bytebin.handler.file.dto.CreateFileRequest
import io.github.frpdoliv3.bytebin.handler.file.dto.CreateFileResponse
import io.github.frpdoliv3.bytebin.handler.file.dto.toCreateFileResponse
import io.github.frpdoliv3.bytebin.service.file.CreateFileResult
import io.github.frpdoliv3.bytebin.service.file.FileService
import io.github.frpdoliv3.bytebin.use_case.UploadPayloadUseCase
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties.Server
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.*

@Component
class FileHandler(
    private val fileService: FileService,
    private val uploadPayloadUseCase: UploadPayloadUseCase
) {
    suspend fun createFile(request: ServerRequest): ServerResponse {
        val requestBody = request.awaitBody(CreateFileRequest::class)
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

    fun uploadFile(fileId: String, @RequestBody requestBody: ByteArray) {
        uploadPayloadUseCase(fileId, requestBody)
    }
}
