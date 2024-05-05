package io.github.frpdoliv3.bytebin.controller.test

import io.github.frpdoliv3.bytebin.repository.FileStorageRepository
import io.github.frpdoliv3.bytebin.repository.JedisChunkCacheRepository
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController(
    private val chunkContentRepo: JedisChunkCacheRepository,
    private val fileStorageRepo: FileStorageRepository
) {
    @PostMapping
    fun action() {
        runBlocking {
            fileStorageRepo.startMultipartUpload("testFile")
        }
    }
}