package org.nryotaro.edgar.text

import org.nryotaro.edgar.exception.EdgarException
import org.springframework.cglib.core.Local
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class IndexParser {

    private val pattern = DateTimeFormatter.ofPattern("MMM dd, uuuu", Locale.US)

    fun parse(index: String) :String {
        val lines =  index.split(Regex("\\r?\\n"))
        val c = extractDate(lines)
       return TODO()
    }

    private fun extractDate(lines: List<String>): LocalDate {
        val dateLine: String = lines.first { it.startsWith("Last Data Received:") }
        val dateStr: String? = Regex("Last Data Received:\\s+(\\w.+)").find(dateLine)?.groupValues?.get(1)
        return if(dateStr != null) LocalDate.parse(dateStr, pattern) else throw EdgarException("failed to find date in \"$dateLine\"")
    }
}
