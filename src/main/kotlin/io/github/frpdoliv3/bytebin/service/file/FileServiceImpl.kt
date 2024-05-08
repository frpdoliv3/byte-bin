package io.github.frpdoliv3.bytebin.service.file

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Option
import io.github.frpdoliv3.bytebin.handler.file.dto.CreateFileRequest
import io.github.frpdoliv3.bytebin.entity.File
import io.github.frpdoliv3.bytebin.repository.FileRepository
import io.github.frpdoliv3.bytebin.repository.FileStorageRepository
import io.github.frpdoliv3.bytebin.service.chunk.ChunkService
import io.github.frpdoliv3.bytebin.util.UUIDUtils
import kotlinx.coroutines.*
import io.github.frpdoliv3.bytebin.model.File as FileModel
import org.springframework.stereotype.Service
import java.util.*
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

    override suspend fun createFile(request: CreateFileRequest): FileService.Outcome<FileModel> {
        try {
            val file = File(
                id = UUIDUtils.createID(),
                name = request.name,
                mimeType = request.mimeType
            )
            val savedFile = withContext(Dispatchers.IO) {
                fileRepo.save(file)
            }
            val createdChunks = chunkService.createChunks(file, request.size)
            return CreateFileResult.Success(
                fileId = savedFile.id,
                createdChunks
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return FileService.Outcome.InternalError
        }
    }

    override fun fileStatus(fileId: String): File.Status? {
        val file = fileRepo.findById(fileId)
        if (file.isEmpty) { return null }
        return file.get().status
    }

    override fun startMultipartUpload(fileId: String): FileService.Outcome<Unit> {
        if (!fileRepo.existsById(fileId)) {
            return FileService.Outcome.FileNotFound
        }
        val statusUpdateResult = startUpload(fileId)
        if (statusUpdateResult !is FileService.Outcome.Success) {
            return statusUpdateResult
        }
        val uploadId = runBlocking {
            fileStorageRepo.startMultipartUpload(fileId)
        }
        if (uploadId == null) {
            fileRepo.findById(fileId).getOrNull()?.let {
                it.status = File.Status.PENDING
                fileRepo.save(it)
            }
            return FileService.Outcome.InternalError
        }
        val file = fileRepo.findById(fileId).getOrNull()!!
        file.uploadId = uploadId
        fileRepo.save(file)
        return FileService.Outcome.Success(Unit)
    }

    override fun startUpload(fileId: String): Either<FileService.Error, Unit> {
        if (!fileRepo.existsById(fileId)) {
            return Left(FileService.Error.FileNotFound)
        }
        val file = fileRepo.findById(fileId).get()
        if (file.status != File.Status.PENDING) {
            return FileService.Outcome.AlreadyUploading
        }
        file.status = File.Status.ONGOING
        fileRepo.save(file)
        return FileService.Outcome.Success(Unit)
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
