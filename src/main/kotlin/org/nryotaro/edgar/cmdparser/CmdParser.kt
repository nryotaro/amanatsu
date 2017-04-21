package org.nryotaro.edgar.cmdparser

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import org.nryotaro.edgar.plainvannila.Arguments
import java.nio.file.Paths
import java.time.LocalDateTime


object CmdParser {

    fun parse(vararg args: String): Arguments {

        val commandLineParser: CommandLineParser = DefaultParser()


        val  option: Options = Options()
        option.addOption("d", "date", true, "foobar")

        //val line: CommandLine = commandLineParser.parse(option, args)
        val line: CommandLine = commandLineParser.parse(option, args)

        return Arguments(LocalDateTime.parse(""), Paths.get(""))

    }
}
