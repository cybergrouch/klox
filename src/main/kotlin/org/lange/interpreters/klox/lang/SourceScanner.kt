package org.lange.interpreters.klox.lang

import org.lange.interpreters.klox.ReporterService

class SourceScanner(
    private val reporterService: ReporterService,
    private val source: String,
    private val sourceCharArray: CharArray = source.toCharArray(),
    private val tokens: MutableList<Token> = mutableListOf(),
    private var start: Int = 0,
    private var current: Int = 0,
    private var line: Int = 1
) {
    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        tokens.add(
            element = Token(
                type = TokenType.EOF,
                lexeme = "",
                line = line
            )
        )
        return tokens.toList()
    }

    private fun scanToken() {
        when (advance()) {
            '(' -> addToken(type = TokenType.LEFT_PARENTHESIS)
            ')' -> addToken(type = TokenType.RIGHT_PARENTHESIS)
            '{' -> addToken(type = TokenType.LEFT_BRACE)
            '}' -> addToken(type = TokenType.RIGHT_BRACE)
            ',' -> addToken(type = TokenType.COMMA)
            '.' -> addToken(type = TokenType.DOT)
            '-' -> addToken(type = TokenType.MINUS)
            '+' -> addToken(type = TokenType.PLUS)
            ';' -> addToken(type = TokenType.SEMICOLON)
            '*' -> addToken(type = TokenType.STAR)

            '!' -> addToken(
                type = matcher(
                    expected = '=',
                    matchHandler = { TokenType.BANG_EQUAL },
                    unmatchHandler = { TokenType.BANG }
                )
            )
            '=' -> addToken(
                type = matcher(
                    expected = '=',
                    matchHandler = { TokenType.EQUAL_EQUAL },
                    unmatchHandler = { TokenType.EQUAL }
                )
            )
            '<' -> addToken(
                type = matcher(
                    expected = '=',
                    matchHandler = { TokenType.LESS_EQUAL },
                    unmatchHandler = { TokenType.LESS }
                )
            )
            '>' -> addToken(
                type = matcher(
                    expected = '=',
                    matchHandler = { TokenType.GREATER_EQUAL },
                    unmatchHandler = { TokenType.GREATER }
                )
            )

            '/' -> matcher(
                expected = '/',
                matchHandler = { lineComment() },
                unmatchHandler = {
                    addToken(type = TokenType.SLASH)
                }
            )

            ' ', '\r', '\t' -> {}

            '\n' -> line++

            '"' -> string()

            else -> reporterService.error(
                line = line,
                message = "Unexpected character."
            )
        }
    }

    private fun lineComment() {
        while (peek() != '\n' && !isAtEnd()) {
            advance()
        }
        parseToTokenType(
            tokenType = TokenType.LINE_COMMENT,
            startIndex = start + 2,
            endIndex = current
        )
    }

    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            peek().takeIf { c -> c == '\n' }?.let { line++ }
            advance()
        }

        isAtEnd().takeIf { it }?.let {
            reporterService.error(
                line = line,
                message = "Unterminated string."
            )
            return@string
        } ?: run {
            advance()
            parseToTokenType(
                tokenType = TokenType.STRING,
                startIndex = start + 1,
                endIndex = current - 1
            )
        }
    }

    private fun <R> matcher(expected: Char, matchHandler: () -> R, unmatchHandler: () -> R): R =
        if (matched(expected = expected)) {
            advance()
            matchHandler()
        } else
            unmatchHandler()

    private fun matched(expected: Char): Boolean =
        when {
            !isAtEnd() && sourceCharArray.elementAt(index = current) == expected -> true
            else -> false
        }

    private fun peek(): Char = if (isAtEnd()) '\u0000' else sourceCharArray.elementAt(current)

    private fun isAtEnd(): Boolean = current >= source.length

    private fun advance(): Char = with(current++) {
        sourceCharArray.elementAt(index = this)
    }

    private fun addToken(type: TokenType, literal: Any? = null) {
        source.subSequence(startIndex = start, endIndex = current).toString().let { lexeme ->
            tokens.add(
                Token(
                    type = type,
                    lexeme = lexeme,
                    literal = literal,
                    line = line
                )
            )
        }
    }

    private fun parseToTokenType(tokenType: TokenType, startIndex: Int, endIndex: Int) =
        when {
            startIndex < endIndex -> source.substring(
                startIndex = startIndex,
                endIndex = endIndex
            )
            else -> ""
        }.let { literal ->
            addToken(
                type = tokenType,
                literal = literal
            )
        }
}
