package org.lange.interpreters.klox

class ReporterServiceImpl(
    private val loggingService: LoggingService
) : ReporterService {
    override fun report(line: Int, where: String?, message: String) {
        with(System.`err`) {
            val errorMessage = "[line $line] Error$where: $message"
            errorMessage.apply {
                println(this)
                loggingService.error(this)
            }
        }
    }
}
