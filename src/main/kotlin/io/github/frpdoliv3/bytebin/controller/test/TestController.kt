package io.github.frpdoliv3.bytebin.controller.test

import io.github.frpdoliv3.bytebin.repository.JedisChunkContentRepository
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.ByteBuffer

@RestController
@RequestMapping("/test")
class TestController(
    private val chunkContentRepo: JedisChunkContentRepository
) {
    @PostMapping
    fun action(@RequestBody content: ByteArray) {
        runBlocking {
            chunkContentRepo.getContent(10).collect {
                print(it.toString(Charsets.UTF_8))
            }
        }
    }
}