package org.nryotaro.edgar.plain.index

import java.net.URL
import java.time.LocalDate


data class Index(val companyName: String, val formType: String, val cik: Int, val dateFiled: LocalDate, val url: String)

data class Indices(val filedDate: LocalDate, val indices: List<Index>)

