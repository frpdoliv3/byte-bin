package io.github.frpdoliv3.bytebin.util

import java.nio.ByteBuffer
import java.util.*

object UUIDUtils {
    private fun asBytes(uuid: UUID): ByteArray {
        val bb = ByteBuffer.allocate(16)
        bb.putLong(uuid.mostSignificantBits)
        bb.putLong(uuid.leastSignificantBits)
        return bb.array()
    }

    private fun sanitizeBase64(id: String): String {
        val sb = StringBuilder(id.length)
        for (c in id.toCharArray()) {
            when (c) {
                '/' -> {
                    sb.append('_')
                }
                '+' -> {
                    sb.append('-')
                }
                '=' -> {
                    sb.append('.')
                }
                else -> {
                    sb.append(c)
                }
            }
        }
        return sb.toString()
    }

    fun createID(): String {
        val uuid = UUID.randomUUID()
        val uuidBytes = asBytes(uuid)
        val base64Id = Base64.getEncoder().encodeToString(uuidBytes)
        return sanitizeBase64(base64Id)
    }
}
