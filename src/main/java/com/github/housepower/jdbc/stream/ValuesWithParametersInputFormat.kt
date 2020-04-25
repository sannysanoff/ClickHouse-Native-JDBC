package com.github.housepower.jdbc.stream

import com.github.housepower.jdbc.data.Block
import com.github.housepower.jdbc.misc.SQLLexer
import com.github.housepower.jdbc.misc.Validate
import java.sql.SQLException

class ValuesWithParametersInputFormat(query: String?, pos: Int) : InputFormat {
    private val lexer: SQLLexer

    override suspend fun fillBlock(block: Block) {
        val constIdx = IntArray(block.columns())
        val nextChar = lexer.character()
        Validate.isTrue(nextChar == '(')
        for (column in 0 until block.columns()) {
            if (column > 0) {
                Validate.isTrue(lexer.character() == ',')
            }
            if (!lexer.isCharacter('?')) {
                block.setConstObject(column, block.getByPosition(column).type()
                        .deserializeTextQuoted(lexer))
                constIdx[column] = 1
            } else {
                lexer.character()
            }
        }
        for (column in 0 until block.columns()) {
            if (constIdx[column] > 0) {
                block.incrIndex(column)
            }
        }
        Validate.isTrue(lexer.character() == ')')
    }

    init {
        lexer = SQLLexer(pos, query!!)
    }
}