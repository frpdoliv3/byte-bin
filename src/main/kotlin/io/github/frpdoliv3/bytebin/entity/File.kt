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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status: Status = Status.PENDING,

    @OneToMany(mappedBy = "file")
    var chunks: Set<Chunk> = emptySet()
) {
    enum class Status {
        PENDING,
        ONGOING,
        DONE
    }
}
