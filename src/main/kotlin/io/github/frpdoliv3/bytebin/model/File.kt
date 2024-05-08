package io.github.frpdoliv3.bytebin.model

sealed class File {
    data class CreatedFile(
        val fileId: String,
        val chunks: List<Chunk.Details>
    )
}
