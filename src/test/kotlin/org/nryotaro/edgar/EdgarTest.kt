package org.nryotaro.edgar

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.nryotaro.edgar.annotation.qualifier.MainRunner
import org.nryotaro.edgar.cmdparser.CmdParser
import org.nryotaro.edgar.retriever.FiledDocumentRetriever
import org.nryotaro.edgar.retriever.FilingDetailRetriever
import org.nryotaro.edgar.retriever.IndexRetriever
import org.nryotaro.edgar.service.FiledDocumentService
import org.nryotaro.edgar.service.FilingDetailService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.rule.OutputCapture
import org.springframework.context.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.function.client.ClientResponse
import kotlin.reflect.KClass

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("ut")
@Import(EdgarRunnerConfiguration::class)
abstract class EdgarTest {
    protected  fun readTextFile(file: String, klass: KClass<out Any>): String {
        return klass.java.getResourceAsStream(file)
                .bufferedReader().use { it.readText() }
    }
}

@Service
@Profile("ut")
@Primary
class EdgarMock: Edgar {
    override fun execute(vararg args: String) {
    }
}

@Configuration
open class EdgarRunnerConfiguration {

    @Bean
    @MainRunner
    open fun edgar(
            @Value("\${spring.application.name}") appName: String,
            cmdParser: CmdParser,
            indexRepository: IndexRetriever,
            filingDetailService: FilingDetailService,
            filedDocumentService: FiledDocumentService): Edgar {
        return EdgarImpl(appName, cmdParser, indexRepository,
                filingDetailService, filedDocumentService)
    }
}

class EdgarBootstrapTest : EdgarTest() {
    val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    @MainRunner
    lateinit var edgar: Edgar

    @get:Rule
    val  outputCapture = OutputCapture()

    val help ="""
    |Missing argument for option: d
    |
    |usage: edgar-crawler -d <arg> [-f] -o <arg>
    |Download documents filed with Edgar
    |
    | -d,--date <arg>               The crawler retrieves the documents
    |                               submitted on the specified date
    | -f,--force                    Download indices and filed documents even
    |                               if they have already existed in the
    |                               specified destination
    | -o,--output-directory <arg>   Retrieved documents will be stored in the
    |                               specified directory
    |
    |Please report issues at https://github.com/nryotaro/edgar-crawler
    |
    """.trimMargin()

    @Test fun error() {
        edgar.execute("-d")
        assertThat(outputCapture.toString(), `is`(help))
    }




}
