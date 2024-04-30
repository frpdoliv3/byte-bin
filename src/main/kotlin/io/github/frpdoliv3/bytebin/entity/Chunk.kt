package io.github.frpdoliv3.bytebin.entity

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    var file: File
)
