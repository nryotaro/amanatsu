package org.nryotaro.edgar.retriever

import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.client.PartialHttpContent
import org.nryotaro.edgar.client.PartialHttpResponse
import org.nryotaro.edgar.client.Status
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
        val destination = FileChannel.open(dest.toPath(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)

        return Mono.just(documentUrl).delayElement(Duration.ofMillis(trafficLimit))
                .flatMapMany{client.getResponse(it) }.reduce(true, {success ,b ->
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
                }}}).doOnNext {
            log.debug("downloaded: ${documentUrl}")
            destination.close()
        }.doOnError {
            destination.close()
            throw EdgarException("failed to download correctly: ${dest}")
        }
    }
}
