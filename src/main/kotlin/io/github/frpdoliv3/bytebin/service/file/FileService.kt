package io.github.frpdoliv3.bytebin.service.file

import io.github.frpdoliv3.bytebin.controller.file.dto.CreateFileRequest

interface FileService {
    fun createFile(request: CreateFileRequest): Boolean
}