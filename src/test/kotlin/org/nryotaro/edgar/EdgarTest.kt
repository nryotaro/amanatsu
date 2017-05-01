package org.nryotaro.edgar

import org.assertj.core.api.Assertions
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.nryotaro.edgar.annotation.qualifier.MainRunner
import org.nryotaro.edgar.cmdparser.CmdParser
import org.nryotaro.edgar.repository.FiledDocumentRepository
import org.nryotaro.edgar.repository.FilingDetailRepository
import org.nryotaro.edgar.repository.IndexRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.rule.OutputCapture
import org.springframework.context.annotation.*
import org.springframework.stereotype.Service
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
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
            cmdParser: CmdParser,
            indexRepository: IndexRepository,
            filingDetailRepository: FilingDetailRepository,
            filedDocumentRepository: FiledDocumentRepository): Edgar {
        return EdgarImpl(cmdParser, indexRepository, filingDetailRepository, filedDocumentRepository)
    }
}

class EdgarBootstrapTest : EdgarTest() {

    @Autowired
    @MainRunner
    lateinit var edgar: Edgar

    @get:Rule
    val  outputCapture = OutputCapture()

    @Test fun execute() {
        edgar.execute("-d")
        assertThat(outputCapture.toString(), `is`(""))
    }
}
