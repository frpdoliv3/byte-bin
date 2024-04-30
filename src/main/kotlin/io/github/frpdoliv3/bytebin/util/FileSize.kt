package io.github.frpdoliv3.bytebin.util

@JvmInline
value class MiB(private val size: Long) {
    fun absoluteSize() = size * 1048576L

    operator fun compareTo(other: MiB) = size.compareTo(other.size)
    operator fun compareTo(other: Long) = absoluteSize().compareTo(other)
}

operator fun Long.compareTo(other: MiB) = this.compareTo(other.absoluteSize())
operator fun Long.plus(other: MiB) = this + other.absoluteSize()
operator fun Long.div(other: MiB) = this / other.absoluteSize()
operator fun Long.rem(other: MiB) = this % other.absoluteSize()

val Long.mib: MiB
    get() = MiB(this)

val Int.mib: MiB
    get() = MiB(this.toLong())
