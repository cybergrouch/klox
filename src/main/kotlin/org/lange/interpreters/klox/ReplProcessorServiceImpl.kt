package org.lange.interpreters.klox

class ReplProcessorServiceImpl(
    private val sourceProcessorService: SourceProcessorService
) : ReplProcessorService {
    override fun run() {
        fun readInLine(prompt: String = "> "): String? {
            print(prompt)
            return readLine()
        }

        var continueLoop = true
        while (continueLoop) {
            readInLine().takeIf { it.isNullOrBlank().not() }?.let { source ->
                sourceProcessorService.process(source = source)
            } ?: run {
                continueLoop = false
            }
        }
    }
}
