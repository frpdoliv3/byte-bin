package io.github.frpdoliv3.bytebin.entity

import jakarta.persistence.*

@Entity
@Table(
    name = "chunks"
)
class Chunk (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,

    @Column(name = "position")
    var position: Int,

    @Column(name = "start_byte")
    var startByte: Long,

    @Column(name = "end_byte")
    var endByte: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    var file: File
)
