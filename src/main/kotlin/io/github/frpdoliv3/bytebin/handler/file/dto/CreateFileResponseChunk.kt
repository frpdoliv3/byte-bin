package io.github.frpdoliv3.bytebin.handler.file.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.frpdoliv3.bytebin.model.Chunk

data class CreateFileResponseChunk(
    @JsonProperty("position") val position: Int,
    @JsonProperty("start_byte") val startByte: Long,
    @JsonProperty("end_byte") val endByte: Long,
    @JsonProperty("length") val length: Long
)

fun Chunk.Details.toCreateFileResponseChunk() = CreateFileResponseChunk(
    position = position,
    startByte = startByte,
    endByte = endByte,
    length = length
)
