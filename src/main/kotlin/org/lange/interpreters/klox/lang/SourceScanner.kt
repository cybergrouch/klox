package org.lange.interpreters.klox.lang

import org.lange.interpreters.klox.ReporterService
import org.lange.interpreters.klox.lang.Constants.DIGIT_RANGE
import org.lange.interpreters.klox.lang.Constants.isAlpha
import org.lange.interpreters.klox.lang.Constants.isAlphaNumeric
import org.lange.interpreters.klox.lang.Constants.isDigit
import org.lange.interpreters.klox.lang.Constants.match

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
                lexeme = Constants.BLANK,
                line = line
            )
        )
        return tokens.toList()
    }

    private fun scanToken() {
        when (val c = advance()) {
            Constants.LEFT_PARENTHESIS -> addToken(type = TokenType.LEFT_PARENTHESIS)
            Constants.RIGHT_PARENTHESIS -> addToken(type = TokenType.RIGHT_PARENTHESIS)
            Constants.LEFT_BRACE -> addToken(type = TokenType.LEFT_BRACE)
            Constants.RIGHT_BRACE -> addToken(type = TokenType.RIGHT_BRACE)
            Constants.COMMA -> addToken(type = TokenType.COMMA)
            Constants.DOT -> addToken(type = TokenType.DOT)
            Constants.MINUS -> addToken(type = TokenType.MINUS)
            Constants.PLUS -> addToken(type = TokenType.PLUS)
            Constants.SEMICOLON -> addToken(type = TokenType.SEMICOLON)
            Constants.STAR -> addToken(type = TokenType.STAR)

            Constants.BANG -> addToken(
                type = matcher(
                    expected = Constants.EQUAL,
                    matchHandler = { TokenType.BANG_EQUAL },
                    unMatchHandler = { TokenType.BANG }
                )
            )
            Constants.EQUAL -> addToken(
                type = matcher(
                    expected = Constants.EQUAL,
                    matchHandler = { TokenType.EQUAL_EQUAL },
                    unMatchHandler = { TokenType.EQUAL }
                )
            )
            Constants.LESS -> addToken(
                type = matcher(
                    expected = Constants.EQUAL,
                    matchHandler = { TokenType.LESS_EQUAL },
                    unMatchHandler = { TokenType.LESS }
                )
            )
            Constants.GREATER -> addToken(
                type = matcher(
                    expected = Constants.EQUAL,
                    matchHandler = { TokenType.GREATER_EQUAL },
                    unMatchHandler = { TokenType.GREATER }
                )
            )

            Constants.SLASH -> matcher(
                expected = Constants.SLASH,
                matchHandler = { lineComment() },
                unMatchHandler = {
                    addToken(type = TokenType.SLASH)
                }
            )

            Constants.SPACE,
            Constants.CARRIAGE_RETURN,
            Constants.TAB -> {
            }

            Constants.NEW_LINE -> line++

            Constants.DOUBLE_QUOTE -> string()

            in DIGIT_RANGE -> number()

            else ->
                when {
                    c.isAlpha() -> identifier()
                    else -> reporterService.error(
                        line = line,
                        message = "Unexpected character: ${peek()}"
                    )
                }
        }
    }

    private fun lineComment() {
        while (peek() != Constants.NEW_LINE && !isAtEnd()) {
            advance()
        }
        parseToTokenType(
            tokenType = TokenType.LINE_COMMENT,
            startIndex = start + 2,
            endIndex = current
        ).appendToList()
    }

    private fun string() {
        while (peek() != Constants.DOUBLE_QUOTE && !isAtEnd()) {
            peek().takeIf { c -> c == Constants.NEW_LINE }?.let { line++ }
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

    private fun number() {
        while (peek().isDigit()) {
            advance()
        }
        when {
            peek().match(expected = Constants.DOT) && peekNext().isDigit() -> {
                advance()
                while (peek().isDigit()) {
                    advance()
                }
                parseToTokenType(
                    tokenType = TokenType.NUMBER,
                    startIndex = start,
                    endIndex = current,
                    literalParser = { literalStr -> literalStr.toDouble() }
                ).appendToList()
            }
            peek().match(expected = Constants.DOT) && peekNext().isDigit().not() -> {
                reporterService.error(
                    line = line,
                    message = "Missing fractional part of number."
                )
            }
            else -> {
                parseToTokenType(
                    tokenType = TokenType.NUMBER,
                    startIndex = start,
                    endIndex = current,
                    literalParser = { literalStr -> literalStr.toDouble() }
                ).appendToList()
            }
        }
    }

    private fun identifier() {
        while (peek().isAlphaNumeric()) advance()
        addToken(type = TokenType.IDENTIFIER)
    }

    private fun <R> matcher(expected: Char, matchHandler: () -> R, unMatchHandler: () -> R): R =
        if (matched(expected = expected)) {
            advance()
            matchHandler()
        } else
            unMatchHandler()

    private fun matched(expected: Char): Boolean =
        when {
            !isAtEnd() && sourceCharArray.elementAt(index = current) == expected -> true
            else -> false
        }

    private fun peek(offset: Int = 0): Char =
        if (isAtEnd(offset = offset)) Constants.NULL else sourceCharArray.elementAt(current + offset)

    private fun peekNext(): Char = peek(offset = 1)

    private fun isAtEnd(offset: Int = 0): Boolean = current + offset >= source.length

    private fun advance(): Char = sourceCharArray.elementAt(index = current++)

    private fun asToken(type: TokenType, lexeme: String = parseLexeme(), literal: Any? = null): Token =
        Token(
            type = type,
            lexeme = lexeme,
            literal = literal,
            line = line
        )

    private fun addToken(type: TokenType, literal: Any? = null) =
        asToken(type = type, literal = literal).appendToList()

    private fun parseToTokenType(
        tokenType: TokenType,
        startIndex: Int,
        endIndex: Int,
        literalParser: (String) -> Any? = { str -> str }
    ) =
        when {
            startIndex < endIndex -> source.substring(
                startIndex = startIndex,
                endIndex = endIndex
            )
            else -> Constants.BLANK
        }.let { literal ->
            asToken(
                type = tokenType,
                literal = literalParser(literal)
            )
        }

    private fun parseLexeme() = source.subSequence(startIndex = start, endIndex = current).toString()

    private fun Token.appendToList() = tokens.add(element = this)
}
