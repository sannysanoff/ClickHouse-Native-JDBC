package com.github.housepower.jdbc.stream

import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.misc.SQLLexer
import com.github.housepower.jdbc.misc.Validate.isTrue
import java.sql.SQLException

class ValuesInputFormat(pos: Int, data: String?) : InputFormat {
    private val lexer: SQLLexer

    override suspend fun fillBlock(block: Block) {
        val constIdx = IntArray(block.columns())
        while (true) {
            var nextChar = lexer.character()
            if (lexer.eof() || nextChar == ';') {
                break
            }
            if (nextChar == ',') {
                nextChar = lexer.character()
            }
            isTrue(nextChar == '(')
            for (column in 0 until block.columns()) {
                if (column > 0) {
                    isTrue(lexer.character() == ',')
                }
                constIdx[column] = 1
                block.setConstObject(column, block.getByPosition(column).type()
                        .deserializeTextQuoted(lexer))
            }
            isTrue(lexer.character() == ')')
            block.appendRow()
        }
        for (column in 0 until block.columns()) {
            if (constIdx[column] > 0) {
                block.incrIndex(column)
            }
        }
    }

    init {
        lexer = SQLLexer(pos, data!!)
    }
}