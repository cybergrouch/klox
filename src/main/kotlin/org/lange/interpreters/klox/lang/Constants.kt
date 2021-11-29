package org.lange.interpreters.klox.lang

object Constants {
    const val LEFT_PARENTHESIS = '('
    const val RIGHT_PARENTHESIS = ')'
    const val LEFT_BRACE = '{'
    const val RIGHT_BRACE = '}'
    const val COMMA = ','
    const val DOT = '.'
    const val MINUS = '-'
    const val PLUS = '+'
    const val SEMICOLON = ';'
    const val STAR = '*'
    const val BANG = '!'
    const val EQUAL = '='
    const val LESS = '<'
    const val GREATER = '>'
    const val SLASH = '/'
    const val DOUBLE_QUOTE = '"'

    const val SPACE = ' '
    const val CARRIAGE_RETURN = '\r'
    const val TAB = '\t'
    const val NEW_LINE = '\n'

    const val NULL = '\u0000'

    const val BLANK = ""

    const val DIGIT_0 = '0'
    const val DIGIT_9 = '9'
    val DIGIT_RANGE = DIGIT_0..DIGIT_9

    const val LETTER_A_LOWERCASE = 'a'
    const val LETTER_Z_LOWERCASE = 'z'
    const val LETTER_A_UPPERCASE = 'A'
    const val LETTER_Z_UPPERCASE = 'Z'
    const val UNDERSCORE = '_'

    fun Char.isDigit(): Boolean = this in DIGIT_RANGE

    fun Char.isAlpha(): Boolean =
        this in LETTER_A_LOWERCASE..LETTER_Z_LOWERCASE ||
            this in LETTER_A_UPPERCASE..LETTER_Z_UPPERCASE ||
            this.match(UNDERSCORE)

    fun Char.isAlphaNumeric(): Boolean =
        this.isDigit() || this.isAlpha()

    fun Char.match(expected: Char): Boolean = (this == expected)
}
