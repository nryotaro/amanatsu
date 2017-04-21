package org.nryotaro.edgar.cmdparser

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import org.nryotaro.edgar.plainvannila.Arguments
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.nio.file.Paths
import java.time.LocalDateTime


@Configurable
class CommandContext {

    @Bean fun options(): Options  {
        val options: Options = Options()
        options.addOption("d", "date", true, "foobar")
        return options
    }
}

@Component
class CmdParser(val options: Options) {

    fun parse(vararg args: String): Arguments {

        val commandLineParser: CommandLineParser = DefaultParser()


        //val line: CommandLine = commandLineParser.parse(option, args)
        val line: CommandLine = commandLineParser.parse(options, args)
        return Arguments(LocalDateTime.parse(""), Paths.get(""))

    }
}
