package org.nryotaro.edgar.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DefaultDataBuffer
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.client.reactive.ClientHttpRequest
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.client.*
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.ipc.netty.http.client.HttpClientException
import java.io.File
import java.nio.channels.FileChannel
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.Duration
import java.time.Duration.ofMillis
import java.time.Duration.ofSeconds
import java.util.function.Function

@Configurable
class EdgarClientContext {

    @Bean fun client(@Value("\${url.root}") edgarRootUrl: String): WebClient {
        return WebClient
                .builder()
                .defaultHeader("Connection", "keep-alive")
                .defaultHeader("Accept-Encoding", "gzip, deflate")
                .baseUrl(edgarRootUrl).build()
    }

}

interface EdgarClient {
    fun getRawResponse(url: String): Mono<ClientResponse>
    fun get(url: String): Mono<String>
    fun getBin(url: String): Mono<DataBuffer>
    fun download(url: String, path: Path): Mono<Boolean>
}

@Service
class EdgarClientImpl(
        val client: WebClient,
        @Value("\${url.root}") val edgarRootUrl: String): EdgarClient {

    val log = LoggerFactory.getLogger(this::class.java)

    override fun getRawResponse(url: String): Mono<ClientResponse> {
       return client.get().uri(url).exchange().timeout(ofSeconds(7L)).retry(3).onErrorResume {
           log.warn("failed to get \"$url\" caused by $it")
           Mono.empty()
       }
    }

    override fun get(url: String): Mono<String> {
        return Mono.just(url).delayElement(ofMillis(100L)).flatMap {
            client.get().uri(it).exchange().timeout(ofSeconds(7L)).onErrorResume {
                log.warn("failed to get \"$url\" caused by $it")
                Mono.empty()
            }.flatMap{
                it.bodyToMono(String::class)
            }.doOnNext{log.debug("downloaded: $url")}
        }
    }

    /**
     * TODO Path -> file
     */
    override fun download(url: String, path: Path): Mono<Boolean> {

        val chan = FileChannel.open(path, StandardOpenOption.WRITE)

        var failure = false

        return client.get().uri(url).exchange().timeout(ofSeconds(7L))
                .onErrorResume {
                    log.warn("failed to download \"$url\" due to $it")
                    Mono.empty()
                }.flatMapMany {
                    it.body(BodyExtractors.toDataBuffers())
        }.doOnNext {
            chan.write(it.asByteBuffer())
        }.doOnError {
            log.error("""Failed to write "$url" to $path""")
            if(!failure) {
                chan.close()
                path.toFile().delete()
            }
            failure = true
        }.doOnNext {
            if(chan.isOpen){
                log.debug("""$url was successfully downloaded""")
                chan.close()
            }
        }.then(Mono.just(!failure))
    }

    private fun cutEdgarRootUrl(url: String): String {
        return url.substringAfter(edgarRootUrl)
    }

    /**
     * TODO write tests
     */
    @Deprecated("not used")
    override fun getBin(url: String): Mono<DataBuffer> {
        return client.get().uri(url).exchange().flatMapMany {
            it.body(BodyExtractors.toDataBuffers())
        }.reduce{acc, b -> acc.write(b)}
    }

}
