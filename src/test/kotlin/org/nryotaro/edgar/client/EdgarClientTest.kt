package org.nryotaro.edgar.client

import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDate

//@Import(EdgarClientContext::class)
class EdgarClientTest : EdgarTest() {

    @Autowired
    lateinit var client: EdgarClient


    @Test fun retrieveIndex()  {
        client.retrieveIndex(LocalDate.parse("2017-03-14"))
    }
}
