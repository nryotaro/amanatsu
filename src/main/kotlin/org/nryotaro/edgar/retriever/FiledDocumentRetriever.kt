package org.nryotaro.edgar.retriever

import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.client.PartialHttpContent
import org.nryotaro.edgar.client.Status
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Repository
class FiledDocumentRetriever(private val client: EdgarClient) {
    val log = LoggerFactory.getLogger(this::class.java)

    fun retrieve(documentUrl: String, dest: File): Mono<Boolean> {
        dest.parentFile.mkdirs()
        val destination = FileChannel.open(dest.toPath(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)

        return client.getResponse(documentUrl).reduce(true,
                {a, b ->
                    if(a) {
                        when(b) {
                            is Status -> if(b.status == 200) {
                                true
                            } else {
                                log.error("status: ${b.status}, url: ${documentUrl}")
                                true
                            }
                            is PartialHttpContent -> {
                                val c: ByteBuffer = ByteBuffer.allocate(b.content.size)
                                c.put(b.content)
                                c.flip()
                                if(!destination.isOpen) {
                                    throw RuntimeException("error close")
                                }
                                destination.write(c)
                                true
                            }
                        }
                    }
                    a}).doOnNext {
            log.error("downloaded correctly: ${dest}")
            destination.close()
        }.doOnError {
            log.error("failed to download correctly: ${dest}")
            destination.close()
        }
    }
}
