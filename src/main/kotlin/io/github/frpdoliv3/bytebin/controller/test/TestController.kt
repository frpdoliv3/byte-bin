package io.github.frpdoliv3.bytebin.controller.test

import io.github.frpdoliv3.bytebin.repository.FileStorageRepository
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
    private val chunkContentRepo: JedisChunkContentRepository,
    private val fileStorageRepo: FileStorageRepository
) {
    @PostMapping
    fun action() {
        runBlocking {
            fileStorageRepo.startMultipartUpload("testFile")
        }
    }
}