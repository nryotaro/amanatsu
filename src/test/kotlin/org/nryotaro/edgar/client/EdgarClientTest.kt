package org.nryotaro.edgar.client

import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.*

class EdgarClientTest: EdgarTest() {

    @Autowired
    lateinit var edgarClient: EdgarClient

    val log =  LoggerFactory.getLogger(this::class.java)
    @Test
    fun download() {
        val dest = Paths.get(createTempFile().toURI())

        log.debug("$dest")

        val c  = edgarClient.download("/Archives/edgar/data/34782/000003478217000039/a1stsourcecorplogo2a03.jpg",
                dest)

        while(c.isOpen) {}
    }
}
