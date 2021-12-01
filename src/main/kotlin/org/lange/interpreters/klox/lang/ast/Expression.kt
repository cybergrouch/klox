package org.lange.interpreters.klox.lang.ast

import org.lange.interpreters.klox.lang.Token

sealed class Expression {
    sealed class Literal<T>(val value: T) : Expression() {
        class Number(value: Double) : Literal<Double>(value = value)
        class String(value: String) : Literal<String>(value = value)
        object True : Literal<Boolean>(value = true)
        object False : Literal<Boolean>(value = false)
        object Nil : Literal<() -> Unit>(value = {})
    }

    class Binary(left: Expression, operator: Token, right: Expression) : Expression()

    class Unary(operator: Token, expression: Expression) : Expression()

    class Grouping(expression: Expression) : Expression()
}
