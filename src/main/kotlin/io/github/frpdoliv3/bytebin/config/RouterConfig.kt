package io.github.frpdoliv3.bytebin.config

import io.github.frpdoliv3.bytebin.handler.file.FileHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfig {

    @Bean
    fun router(
        fileHandler: FileHandler
    ) = coRouter {
        accept(MediaType.APPLICATION_JSON).nest {
            "/files".nest {
                POST("", fileHandler::createFile)
                PUT("{file_id}", fileHandler::uploadFile)
            }
        }
    }
}