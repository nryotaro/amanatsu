package org.nryotaro.edgar

import org.junit.runner.RunWith
import org.nryotaro.edgar.client.EdgarClientContext
import org.nryotaro.edgar.cmdparser.CommandContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("ut")
abstract class EdgarTest {}

@Service
@Profile("ut")
class EdgarMock: Edgar {
    override fun execute(vararg args: String) {
    }
}
