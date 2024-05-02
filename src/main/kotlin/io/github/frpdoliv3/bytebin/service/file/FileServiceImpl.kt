package io.github.frpdoliv3.bytebin.service.file

import io.github.frpdoliv3.bytebin.controller.file.dto.CreateFileRequest
import io.github.frpdoliv3.bytebin.entity.File
import io.github.frpdoliv3.bytebin.repository.FileRepository
import io.github.frpdoliv3.bytebin.repository.FileStorageRepository
import io.github.frpdoliv3.bytebin.service.chunk.ChunkService
import io.github.frpdoliv3.bytebin.util.UUIDUtils
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class FileServiceImpl(
    private val fileRepo: FileRepository,
    private val chunkService: ChunkService,
    private val fileStorageRepo: FileStorageRepository,
): FileService {
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

    override fun startMultipartUpload(fileId: String) {
        if (!fileRepo.existsById(fileId)) {
            return
        }
        startUpload(fileId)
        val uploadId = runBlocking {
            fileStorageRepo.startMultipartUpload(fileId)
        }
        val file = fileRepo.findById(fileId)
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
}
