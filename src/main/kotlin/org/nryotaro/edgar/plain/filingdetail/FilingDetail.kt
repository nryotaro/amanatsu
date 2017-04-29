package org.nryotaro.edgar.plain.filingdetail

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser


data class Document(val filename: String, val url: String)

data class FilingDetail(val seq: Int?, val description: String, val document: Document, val type: String, val size: Int?)
