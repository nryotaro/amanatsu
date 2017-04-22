package org.nryotaro.edgar

import org.springframework.stereotype.Service
import com.sun.xml.internal.fastinfoset.util.StringArray
import org.nryotaro.edgar.cmdparser.CmdParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication

import org.springframework.boot.SpringApplication
import java.util.Arrays.asList

@Service
class Sv(@Value("\${foo.bar}") val c: Int) {

}