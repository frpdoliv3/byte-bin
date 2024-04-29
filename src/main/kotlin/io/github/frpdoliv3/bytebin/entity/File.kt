package io.github.frpdoliv3.bytebin.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "files")
class File(
    @Id
    @Column(columnDefinition = "VARCHAR(24)")
    var id: String?,

    @Column(name = "name")
    var name: String,

    @Column(name = "size")
    var size: Long,

    @Column(name = "chunk_size")
    var chunkSize: Long,

    @Column(name = "mime_type")
    var mimeType: String,

    @OneToMany(mappedBy = "file")
    var chunks: Set<Chunk>
)
