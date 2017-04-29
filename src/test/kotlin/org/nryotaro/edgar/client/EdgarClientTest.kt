package org.nryotaro.edgar.client

import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.springframework.beans.factory.annotation.Autowired
import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.*

class EdgarClientTest: EdgarTest() {

    @Autowired
    lateinit var edgarClient: EdgarClient

    @Test
    fun getBin() {
        File("/Users/nryotaro/hoge.png").createNewFile()

        /*
        FileChannel.open(Paths.get("/Users/nryotaro/hoge.png"), StandardOpenOption.WRITE)
                .write(edgarClient.getBin(
                        "Archives/edgar/data/34782/000003478217000039/a1stsourcecorplogo2a03.jpg").block().asByteBuffer())
                        */

        edgarClient.download("Archives/edgar/data/34782/000003478217000039/a1stsourcecorplogo2a03.jpg",
                Paths.get("/Users/nryotaro/hoge.png"))

        Thread.sleep(1000*10)

    }
}
