package org.nryotaro.edgar.retriever

import io.netty.handler.timeout.ReadTimeoutException
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.`when`
import org.nryotaro.edgar.EdgarTest
import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.client.LastHttpContent
import org.nryotaro.edgar.client.PartialHttpResponse
import org.nryotaro.edgar.client.Status
import org.nryotaro.edgar.exception.EdgarException
import org.nryotaro.edgar.retriever.FiledDocumentRetriever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import reactor.core.publisher.Flux
import java.time.Duration

class FiledDocumentRetrieverTest : EdgarTest() {


    @Autowired
    lateinit var filedDocumentRetriever: FiledDocumentRetriever

    @MockBean
    lateinit var client: EdgarClient

    @Test
    fun retrieve(){
        val url = "http://Archives/edgar/data/1535323/000162828017002553/xslForm13F_X01/allianzasset13-fhrq4inform.xml"

         `when`(client.getResponse(url)).thenReturn(
                Flux.create<PartialHttpResponse>{
                    it.next(Status(200))
                    it.next(LastHttpContent("foobar".toByteArray()))
                    it.complete()
                }
        )

        val dest = createTempFile()
        dest.delete()
        filedDocumentRetriever.retrieve(url, dest).block()

        assertThat(dest.readText(), `is`("foobar"))
        /*
        `when`(client.getResponse(url)).thenReturn(
                Flux.create<PartialHttpResponse> {
                    it.error(ReadTimeoutException.INSTANCE)
                }
        ).thenReturn(
                Flux.create<PartialHttpResponse>{
                    it.next(Status(200))
                    it.next(LastHttpContent("foobar".toByteArray()))
                }
        )
        */
    }
}
