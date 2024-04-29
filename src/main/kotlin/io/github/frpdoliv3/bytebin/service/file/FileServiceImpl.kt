package io.github.frpdoliv3.bytebin.service.file

import io.github.frpdoliv3.bytebin.controller.file.dto.CreateFileRequest
import io.github.frpdoliv3.bytebin.entity.File
import io.github.frpdoliv3.bytebin.repository.FileRepository
import io.github.frpdoliv3.bytebin.util.UUIDUtils
import org.springframework.stereotype.Service

@Service
class FileServiceImpl(
    private val fileRepo: FileRepository
): FileService {
    override fun createFile(request: CreateFileRequest): Boolean {
        try {
            val file = File(
                id = UUIDUtils.createID(),
                name = request.name,
                mimeType = request.mimeType,
                size = request.size,
                //TODO: support big files
                chunkSize = request.size
            )
            fileRepo.save(file)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}
