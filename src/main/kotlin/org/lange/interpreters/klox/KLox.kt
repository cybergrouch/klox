package org.lange.interpreters.klox

import kotlin.system.exitProcess

object KLox {
    fun main(args: Array<String>) {
        when {
            args.size > 1 -> {
                println("Usage: klox [script]")
                exitProcess(64)
            }
            args.size == 1 -> {
                processFile(args[0])
            }
            else -> {
                runREPL()
            }
        }
    }

    private fun runREPL() {
        fun readInLine(prompt: String = "> "): String? {
            print(prompt)
            return readLine()
        }

        var continueLoop = true
        while (continueLoop) {
            readInLine()?.takeIf { it.isNullOrBlank().not() }?.let { source ->
                runSource(source = source)
            } ?: run {
                continueLoop = false
            }
        }
    }

    private fun runSource(source: String) {
        println("Run: $source")
    }

    private fun processFile(path: String) {
        println("Running Source Path: $path")
    }
}
