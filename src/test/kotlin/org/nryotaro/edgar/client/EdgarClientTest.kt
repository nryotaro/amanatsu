package org.nryotaro.edgar.client

import org.junit.Test
import org.mockito.Mockito.`when`
import org.nryotaro.edgar.EdgarTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDate

//@Import(EdgarClientContext::class)
class EdgarClientTest : EdgarTest() {

    @Autowired
    lateinit var edgarClient: EdgarClient

    /*
    @MockBean
    lateinit var webClient: WebClient

    @MockBean
    lateinit  var spec: WebClient.UriSpec<WebClient.RequestHeadersSpec<*>>
    */

    @Test fun retrieveIndex()  {
        //`when`(webClient.get()).thenReturn(c)
//        `when`(webClient.get()).thenThrow(UnsupportedOperationException("asdfasdfasdfa"))

    }
}
