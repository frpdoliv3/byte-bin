package io.github.frpdoliv3.bytebin.entity

import io.github.frpdoliv3.bytebin.model.Chunk as ChunkModel
import io.github.frpdoliv3.bytebin.model.ChunkStatus
import jakarta.persistence.*

@Entity
@Table(
    name = "chunks"
)
class Chunk (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(name = "position")
    var position: Int,

    @Column(name = "start_byte")
    var startByte: Long,

    @Column(name = "end_byte")
    var endByte: Long,

    @Column(name = "length")
    var length: Long,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status: ChunkStatus = ChunkStatus.PENDING,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    var file: File
) {
    fun toModel(): ChunkModel.Details = ChunkModel.Details(
        id = id!!,
        position = position,
        startByte = startByte,
        endByte = endByte,
        length = length,
        status = status,
        fileId = file.id
    )
}
