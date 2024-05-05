package io.github.frpdoliv3.bytebin.entity

import jakarta.persistence.*

@Entity
@Table(name = "files")
class File(
    @Id
    @Column(columnDefinition = "VARCHAR(24)")
    var id: String,

    @Column(name = "name")
    var name: String,

    @Column(name = "mime_type")
    var mimeType: String,

    @Column(name = "upload_id", nullable = true)
    var uploadId: String? = null,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status: Status = Status.PENDING,

    //TODO: remove eager loading and replace this by domain models
    @OneToMany(mappedBy = "file", fetch = FetchType.EAGER)
    var chunks: Set<Chunk> = emptySet()
) {
    enum class Status {
        PENDING,
        ONGOING,
        DONE
    }
}
