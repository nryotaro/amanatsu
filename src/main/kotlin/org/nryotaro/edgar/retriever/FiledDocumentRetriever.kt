package org.nryotaro.edgar.retriever

import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.client.PartialHttpContent
import org.nryotaro.edgar.client.Status
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Repository
class FiledDocumentRetriever(private val client: EdgarClient) {

    fun retrieve(documentUrl: String, dest: File): Mono<Boolean> {
        dest.parentFile.mkdirs()
        dest.createNewFile()
        val destination = FileChannel.open(dest.toPath(), StandardOpenOption.WRITE)

        return client.getResponse(documentUrl).reduce(true,
                {a, b ->
                    if(a) {
                        when(b) {
                            is Status -> b.status == 200
                            is PartialHttpContent -> {
                                val c: ByteBuffer = ByteBuffer.allocate(b.content.size)
                                destination.write(c)
                                true
                            }
                        }
                    }
                    a}).doOnNext {
            destination.close()
        }.doOnError { destination.close() }
    }
}
