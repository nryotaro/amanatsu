package org.nryotaro.edgar.retriever

import org.junit.Test
import org.mockito.Mockito.`when`
import org.nryotaro.edgar.EdgarTest
import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.retriever.FiledDocumentRetriever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import reactor.core.publisher.Flux

class FiledDocumentRetrieverTest : EdgarTest() {


    @Autowired
    lateinit var filedDocumentRetriever: FiledDocumentRetriever

    @MockBean
    lateinit var client: EdgarClient

    /**
     * TODO write test
     */
    @Test
    fun retrieve(){

        //`when`(client.getResponse("any")).thenReturn(Flux.just())
        //filedDocumentRetriever.retrieve("any", createTempFile())

    }
}
