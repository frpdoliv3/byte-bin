package io.github.frpdoliv3.bytebin.controller.file.dto

data class CreateFileRequest(
    val name: String,
    val mimeType: String,
    val size: Long,
)
