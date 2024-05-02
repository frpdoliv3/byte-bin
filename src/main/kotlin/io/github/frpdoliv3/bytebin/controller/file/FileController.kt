package io.github.frpdoliv3.bytebin.controller.file

import io.github.frpdoliv3.bytebin.controller.file.dto.CreateFileRequest
import io.github.frpdoliv3.bytebin.service.file.FileService
import io.github.frpdoliv3.bytebin.use_case.UploadPayloadUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/files")
class FileController(
    private val fileService: FileService,
    private val uploadPayloadUseCase: UploadPayloadUseCase
) {
    @PostMapping
    fun createFile(@RequestBody requestBody: CreateFileRequest): ResponseEntity<Unit> {
        if (fileService.createFile(requestBody)) {
            return ResponseEntity.status(HttpStatus.CREATED).build()
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }

    @PutMapping("{file_id}")
    fun uploadFile(@PathVariable("file_id") fileId: String, @RequestBody requestBody: ByteArray) {
        uploadPayloadUseCase(fileId, requestBody)
    }
}