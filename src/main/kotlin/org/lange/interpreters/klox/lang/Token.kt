package org.lange.interpreters.klox.lang

data class Token(
    val type: TokenType,
    val lexeme: String,
    val literal: Any? = null,
    val line: Int
) {
    override fun toString(): String {
        return "Token(type=$type, lexeme='$lexeme', literal=$literal, line=$line)"
    }
}
