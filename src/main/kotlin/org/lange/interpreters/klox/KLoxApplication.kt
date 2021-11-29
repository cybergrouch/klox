package org.lange.interpreters.klox

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.system.exitProcess

class KLoxApplication : KoinComponent {

    private val fileProcessorService by inject<FileProcessorService>()

    private val replProcessorService by inject<ReplProcessorService>()

    fun main(args: Array<String>) {
        when {
            args.size > 1 -> {
                println("Usage: klox [script]")
                exitProcess(64)
            }
            args.size == 1 -> {
                fileProcessorService.processFile(args[0])
            }
            else -> {
                replProcessorService.run()
            }
        }
    }
}
