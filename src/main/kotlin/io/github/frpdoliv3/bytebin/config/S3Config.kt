package io.github.frpdoliv3.bytebin.config

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.net.url.Url
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config {
    @Bean
    fun createS3Client(): S3Client = S3Client {
        credentialsProvider = StaticCredentialsProvider.Builder().apply {
            accessKeyId = "test"
            secretAccessKey = "test"
        }.build()
        endpointUrl = Url.parse("https://s3.localhost.localstack.cloud:4566")
        region = "us-east-1"
    }
}
