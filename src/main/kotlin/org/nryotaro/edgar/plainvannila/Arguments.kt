package org.nryotaro.edgar.plainvannila

import java.io.File
import java.nio.file.Path
import java.time.LocalDate
import java.time.LocalDateTime

data class Arguments(val date: LocalDate, val destination: File)
