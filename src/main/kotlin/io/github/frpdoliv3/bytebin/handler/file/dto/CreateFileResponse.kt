package io.github.frpdoliv3.bytebin.handler.file.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.frpdoliv3.bytebin.service.file.CreateFileResult

data class CreateFileResponse(
    @JsonProperty("file_id") val fileId: String,
    @JsonProperty("chunks") val chunks: List<CreateFileResponseChunk>
)

fun CreateFileResult.Success.toCreateFileResponse() = CreateFileResponse(
    fileId = fileId,
    chunks = chunks.map { it.toCreateFileResponseChunk() }
)
