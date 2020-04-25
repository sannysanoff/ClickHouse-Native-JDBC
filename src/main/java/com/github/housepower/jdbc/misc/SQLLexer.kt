package com.github.housepower.jdbc.misc

import com.github.housepower.jdbc.misc.Validate.isTrue
import java.sql.SQLException

class SQLLexer(private var pos: Int, data: String) {
    private val data: CharArray
    fun character(): Char {
        return if (eof()) 0.toChar() else data[pos++]
    }

    fun longLiteral(): Number {
        skipAnyWhitespace()
        val start = pos
        var isHex = false
        if (isCharacter('-') || isCharacter('+')) pos++
        if (pos + 2 < data.size) {
            if (data[pos] == '0' && (Character.toLowerCase(data[pos + 1]) == 'x'
                            || Character.toLowerCase(data[pos + 1]) == 'b')) {
                pos += 2
                isHex = Character.toLowerCase(data[pos + 1]) == 'x'
            }
        }
        while (pos < data.size) {
            if (if (isHex) !isHexDigit(data[pos]) else !isNumericASCII(data[pos])) {
                break
            }
            pos++
        }
        return java.lang.Long.valueOf(StringView(start, pos, data).toString())
    }

    fun numberLiteral(): Number {
        skipAnyWhitespace()
        val start = pos
        var isHex = false
        var isDouble = false
        if (isCharacter('-') || isCharacter('+')) pos++
        if (pos + 2 < data.size) {
            if (data[pos] == '0' && (Character.toLowerCase(data[pos + 1]) == 'x'
                            || Character.toLowerCase(data[pos + 1]) == 'b')) {
                pos += 2
                isHex = Character.toLowerCase(data[pos + 1]) == 'x'
            }
        }
        while (pos < data.size) {
            if (if (isHex) !isHexDigit(data[pos]) else !isNumericASCII(data[pos])) {
                break
            }
            pos++
        }
        if (pos < data.size && data[pos] == '.') {
            isDouble = true
            pos++
            while (pos < data.size) {
                if (if (isHex) !isHexDigit(data[pos]) else !isNumericASCII(data[pos])) break
                pos++
            }
        }
        if (pos + 1 < data.size && (if (isHex) data[pos] == 'p' || data[pos] == 'P' else data[pos] == 'E' || data[pos] == 'e')) {
            pos++
            if (pos + 1 < data.size && (data[pos] == '-' || data[pos] == '+')) {
                pos++
            }
            while (pos < data.size) {
                val ch = data[pos]
                if (!isNumericASCII(ch)) {
                    break
                }
                pos++
            }
        }
        return if (isDouble) java.lang.Double.valueOf(StringView(start, pos, data).toString()) else java.lang.Long.valueOf(StringView(start, pos, data).toString())
    }

    @Throws(SQLException::class)
    fun stringLiteral(): StringView {
        skipAnyWhitespace()
        isTrue(isCharacter('\''))
        return stringLiteralWithQuoted('\'')
    }

    fun eof(): Boolean {
        skipAnyWhitespace()
        return pos >= data.size
    }

    fun isCharacter(ch: Char): Boolean {
        return !eof() && data[pos] == ch
    }

    @Throws(SQLException::class)
    fun bareWord(): StringView {
        skipAnyWhitespace()
        if (isCharacter('`')) {
            return stringLiteralWithQuoted('`')
        } else if (isCharacter('"')) {
            return stringLiteralWithQuoted('"')
        } else if ('_' == data[pos] || data[pos] >= 'a' && data[pos] <= 'z' || (data[pos] >= 'A'
                        && data[pos] <= 'Z')) {
            val start = pos
            pos++
            while (pos < data.size) {
                if (!('_' == data[pos] || data[pos] >= 'a' && data[pos] <= 'z' || (data[pos] >= 'A'
                                && data[pos] <= 'Z') || data[pos] >= '0' && data[pos] <= '9')) break
                pos++
            }
            return StringView(start, pos, data)
        }
        throw SQLException("Expect Bare Token.")
    }

    val isWhitespace: Boolean
        get() = data[pos++] == ' '

    private fun isNumericASCII(c: Char): Boolean {
        return c >= '0' && c <= '9'
    }

    private fun isHexDigit(c: Char): Boolean {
        return isNumericASCII(c) || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F'
    }

    private fun skipAnyWhitespace() {
        while (pos < data.size) {
            if (!(data[pos] == ' ' || data[pos] == '\t' || data[pos] == '\n' || data[pos] == '\r')) {
                return
            }
            pos++
        }
    }

    @Throws(SQLException::class)
    private fun stringLiteralWithQuoted(quoted: Char): StringView {
        val start = pos
        isTrue(data[pos] == quoted)
        pos++
        while (pos < data.size) {
            if (data[pos] == '\\') pos++ else if (data[pos] == quoted) return StringView(start + 1, pos++, data)
            pos++
        }
        throw SQLException("The String Literal is no Closed.")
    }

    init {
        this.data = data.toCharArray()
    }
}