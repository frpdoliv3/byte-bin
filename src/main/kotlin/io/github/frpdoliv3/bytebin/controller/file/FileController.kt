package io.github.frpdoliv3.bytebin.controller.file

import io.github.frpdoliv3.bytebin.controller.file.dto.CreateFileRequest
import io.github.frpdoliv3.bytebin.service.file.FileService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/files")
class FileController(
    private val fileService: FileService
) {
    @PostMapping
    fun createFile(@RequestBody requestBody: CreateFileRequest): ResponseEntity<Unit> {
        if (fileService.createFile(requestBody)) {
            return ResponseEntity.status(HttpStatus.CREATED).build()
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }
}