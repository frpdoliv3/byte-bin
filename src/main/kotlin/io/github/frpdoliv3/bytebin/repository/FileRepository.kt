package io.github.frpdoliv3.bytebin.repository

import io.github.frpdoliv3.bytebin.entity.File
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository: JpaRepository<File, String>
