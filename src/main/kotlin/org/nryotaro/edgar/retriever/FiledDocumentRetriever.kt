package org.nryotaro.edgar.retriever

import org.nryotaro.edgar.client.*
import org.nryotaro.edgar.exception.EdgarException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption
import java.time.Duration

@Repository
class FiledDocumentRetriever(
        @Value("\${edgar.traffic.limit}") private val trafficLimit: Long,
        private val client: EdgarClient) {
    val log = LoggerFactory.getLogger(this::class.java)

    /**
     * TODO handle the case dest exists
     */
    fun retrieve(documentUrl: String, dest: File): Mono<Boolean> {
        dest.parentFile.mkdirs()
        var destination = FileChannel.open(dest.toPath(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)

        return Mono.just(documentUrl).delayElement(Duration.ofMillis(trafficLimit))
                .flatMapMany{ client.getResponse(it) }.reduce(true, {success ,b ->
            when(success) {
                false -> false
                true -> when(b) {
                    is Status -> if(b.status == 200)
                        true
                    else {
                        log.error("status: ${b.status}, url: $documentUrl")
                        false
                    }
                    is PartialHttpContent -> {
                        val c: ByteBuffer = ByteBuffer.allocate(b.content.size)
                        c.put(b.content)
                        c.flip()
                        destination.write(c)
                        true
                    }
                    is LastHttpContent -> {
                        val c: ByteBuffer = ByteBuffer.allocate(b.content.size)
                        c.put(b.content)
                        c.flip()
                        destination.write(c)
                        destination.close()
                        true
                    }
                }}}).doOnError {
            log.debug("$it occurred.")
            dest.delete()
            destination = FileChannel.open(dest.toPath(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)
        }.retry(3).doOnNext{log.debug("downloaded $documentUrl")}.onErrorResume {
            destination.close()
            dest.delete()
            log.error("failed to download correctly: ${documentUrl}")
            Mono.just(false)
        }
    }
}
