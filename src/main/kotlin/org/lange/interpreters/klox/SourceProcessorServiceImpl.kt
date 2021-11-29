package org.lange.interpreters.klox

import org.lange.interpreters.klox.lang.SourceScanner

class SourceProcessorServiceImpl(
    private val reporterService: ReporterService
) : SourceProcessorService {
    override fun process(source: String) {
        SourceScanner(
            reporterService = reporterService,
            source = source
        ).scanTokens().forEach { token ->
            println("Token: $token")
        }
    }
}
