package com.github.housepower.jdbc.stream

import com.github.housepower.jdbc.data.Block
import java.sql.SQLException

interface InputFormat {
    suspend fun fillBlock(block: Block)
}