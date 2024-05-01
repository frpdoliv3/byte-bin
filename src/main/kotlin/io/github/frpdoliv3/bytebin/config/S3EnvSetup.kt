package io.github.frpdoliv3.bytebin.config

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.CreateBucketRequest
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class S3EnvSetup(
    private val s3Client: S3Client
): ApplicationRunner {
    private val LOG = LoggerFactory.getLogger(S3EnvSetup::class.java)

    override fun run(args: ApplicationArguments?): Unit = runBlocking {
        LOG.info("Creating S3 bucket")
        val createBucketRequest = CreateBucketRequest {
            bucket = "byte-bin-files"
        }
        s3Client.createBucket(createBucketRequest)
    }
}