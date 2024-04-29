package io.github.frpdoliv3.bytebin

import io.github.frpdoliv3.bytebin.util.UUIDUtils
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ByteBinApplication

fun main(args: Array<String>) {
    runApplication<ByteBinApplication>(*args)
}
