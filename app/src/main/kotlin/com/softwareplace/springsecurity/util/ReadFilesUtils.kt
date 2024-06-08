package com.softwareplace.springsecurity.util

import com.softwareplace.jsonlogger.log.kLogger
import org.springframework.core.io.ResourceLoader
import java.io.File
import java.nio.file.Files

class ReadFilesUtils {
    companion object {
        fun readFileBytes(resourceLoader: ResourceLoader, filePath: String): ByteArray {
            kLogger.info("Reading content from file: $filePath")
            val resource = resourceLoader.getResource(filePath)

            val fileContent = if (resource.exists()) {
                resource.inputStream.use { it.readBytes() }
            } else {
                val file = File(filePath)
                if (file.exists()) {
                    return Files.readAllBytes(file.toPath())
                }
                throw IllegalArgumentException("File not found: $filePath")
            }

            kLogger.info("Content read from file: ${fileContent.size} bytes")
            return fileContent
        }
    }
}
