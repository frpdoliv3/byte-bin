package io.github.frpdoliv3.bytebin.service.file

import io.github.frpdoliv3.bytebin.controller.file.dto.CreateFileRequest
import io.github.frpdoliv3.bytebin.entity.File
import io.github.frpdoliv3.bytebin.repository.FileRepository
import io.github.frpdoliv3.bytebin.repository.FileStorageRepository
import io.github.frpdoliv3.bytebin.service.chunk.ChunkService
import io.github.frpdoliv3.bytebin.util.UUIDUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class FileServiceImpl(
    private val fileRepo: FileRepository,
    private val chunkService: ChunkService,
    private val fileStorageRepo: FileStorageRepository,
): FileService {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            chunkService.events.collect { handleChunkEvents(it) }
        }
    }

    override fun createFile(request: CreateFileRequest): Boolean {
        try {
            val file = File(
                id = UUIDUtils.createID(),
                name = request.name,
                mimeType = request.mimeType
            )
            fileRepo.save(file)
            chunkService.createChunks(file, request.size)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override fun fileStatus(fileId: String): File.Status? {
        val file = fileRepo.findById(fileId)
        if (file.isEmpty) { return null }
        return file.get().status
    }

    override fun startMultipartUpload(fileId: String) {
        if (!fileRepo.existsById(fileId)) {
            return
        }
        startUpload(fileId)
        val uploadId = runBlocking {
            fileStorageRepo.startMultipartUpload(fileId)
        }
        val fileFindResult = fileRepo.findById(fileId)
        if (fileFindResult.isEmpty) { return }
        val file = fileFindResult.get()
        file.uploadId = uploadId
        fileRepo.save(file)
    }

    override fun startUpload(fileId: String) {
        if (!fileRepo.existsById(fileId)) {
            TODO("Implement file not found")
        }
        val file = fileRepo.findById(fileId).get()
        if (file.status != File.Status.PENDING) {
            TODO("Implement file already uploading")
        }
        file.status = File.Status.ONGOING
        fileRepo.save(file)
    }

    override fun isMultipart(fileId: String): Boolean? {
        val file = fileRepo.findById(fileId)
        if (file.isEmpty) {
            return null
        }
        return file.get().chunks.size > 1
    }

    override fun upload(fileId: String, payload: ByteArray) {
        val file = fileRepo.findById(fileId).getOrNull() ?: return
        runBlocking { chunkService.uploadChunkPayload(fileId, payload) }
    }

    private suspend fun handleChunkUploadedEvent(event: ChunkService.Event.ChunkUploaded) {
        val file: File = chunkService.findFileByChunkId(event.chunkId) ?: return
        if (file.chunks.size == 1) {
            val chunkData = chunkService.loadChunkData(event.chunkId)
            print(fileStorageRepo.uploadFile(file.id, chunkData.payload, chunkData.contentLength))
        }
    }

    private suspend fun handleChunkEvents(event: ChunkService.Event) = when(event) {
        is ChunkService.Event.ChunkUploaded -> handleChunkUploadedEvent(event)
    }
}
