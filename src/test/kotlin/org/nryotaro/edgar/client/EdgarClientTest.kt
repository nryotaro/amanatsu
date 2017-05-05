package org.nryotaro.edgar.client

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.nryotaro.edgar.EdgarTest
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.*
import java.util.function.Function

class EdgarClientTest: EdgarTest() {

    @Autowired
    lateinit var edgarClient: EdgarClient

    @MockBean
    lateinit var client: WebClient

    @MockBean
    lateinit var spec: WebClient.UriSpec<WebClient.RequestHeadersSpec<*>>

    @MockBean
    lateinit var reqSpec: WebClient.RequestHeadersSpec<*>
    @MockBean
    lateinit var resp: ClientResponse

    val log =  LoggerFactory.getLogger(this::class.java)

    @Test
    fun download() {
        val dest = Paths.get(createTempFile().toURI())

        log.debug("$dest")

        `when`(client.get()).thenReturn(spec)
        `when`(spec.uri(anyString())).thenReturn(reqSpec)
        `when`(reqSpec.exchange()).thenReturn(Mono.just(resp))
        val arrays: ByteBuffer = ByteBuffer.wrap(byteArrayOf('a'.toByte()))
        `when`(resp.body(BodyExtractors.toDataBuffers())).thenReturn(Flux.just(DefaultDataBufferFactory().wrap(arrays)))

        val c  = edgarClient.download("exist/path", dest)
        while(c.isOpen) {}

        assertThat(File(dest.toUri()).readText(), `is`("a"))
    }
    @Test
    fun downloadOnError() {
        val dest = Paths.get(createTempFile().toURI())

        log.debug("$dest")

        `when`(client.get()).thenReturn(spec)
        `when`(spec.uri(anyString())).thenReturn(reqSpec)
        `when`(reqSpec.exchange()).thenReturn(Mono.just(resp))

        `when`(resp.body(BodyExtractors.toDataBuffers()))
                .thenThrow(RuntimeException())

        val c  = edgarClient.download("exist/path", dest)
        while(c.isOpen) {}

        assertThat(dest.toFile().exists(), `is`(false))
    }


}
