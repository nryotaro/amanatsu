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
import java.util.function.Function

@Configurable
class EdgarClientContext {

    @Bean fun client(@Value("\${url.root}") edgarRootUrl: String): WebClient {
        return WebClient.builder().defaultHeader("Connection", "keep-alive").baseUrl(edgarRootUrl).build()
        /*
        val conn = ReactorClientHttpConnector()
        val strategies = ExchangeStrategies.withDefaults()

        return WebClient.builder().exchangeStrategies(strategies).baseUrl(edgarRootUrl)
                .clientConnector(conn).defaultHeader("Connection", "keep-alive").exchangeFunction {
            conn.connect(it.method(), it.url(),{a: ClientHttpRequest -> it.writeTo(a, strategies)}).map {
            }
        }.build()
        */
    }

}

interface EdgarClient {
    fun getRawResponse(url: String): Mono<ClientResponse>
    fun get(url: String): Mono<String>
    fun getBin(url: String): Mono<DataBuffer>
    fun download(url: String, path: Path): FileChannel
}

@Service
class EdgarClientImpl(
        val client: WebClient,
        @Value("\${url.root}") val edgarRootUrl: String): EdgarClient {

    val log  = LoggerFactory.getLogger(this::class.java)

    override fun getRawResponse(url: String): Mono<ClientResponse> {
       return client.get().uri(url).exchange()
    }

    override fun get(url: String): Mono<String> {
        return client.get().uri(url).exchange().
                timeout(Duration.ofSeconds(10)).retry(3).onErrorResume {
            if(it is HttpClientException) {
                log.debug("${it.message()}")
            }
            log.debug("failed to get \"$url\" caused by $it")
            Mono.empty()
        }.
                flatMap{
            it.bodyToMono(String::class)
        }

    }

    /**
     * TODO write tests
     */
    override fun getBin(url: String): Mono<DataBuffer> {
        return client.get().uri(url).exchange().flatMapMany {
            it.body(BodyExtractors.toDataBuffers())
        }.reduce{acc, b -> acc.write(b)}
    }

    /**
     * TODO Path -> file
     */
    override fun download(url: String, path: Path): FileChannel {
        val chan = FileChannel.open(path, StandardOpenOption.WRITE)

        client.get().uri(url).exchange().flatMapMany {
            it.body(BodyExtractors.toDataBuffers())
        }.subscribe({chan.write(it.asByteBuffer())},
                {log.error("""Failed to download "$url"""")
                    chan.close()
                    path.toFile().delete()},
                {chan.close()})
        return chan
    }

    private fun cutEdgarRootUrl(url: String): String {
        return url.substringAfter(edgarRootUrl)
    }
}