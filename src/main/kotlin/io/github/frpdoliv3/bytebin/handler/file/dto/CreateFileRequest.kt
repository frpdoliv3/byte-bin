package io.github.frpdoliv3.bytebin.handler.file.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateFileRequest(
    @JsonProperty("name") val name: String,
    @JsonProperty("mime_type") val mimeType: String,
    @JsonProperty("size") val size: Long,
)
