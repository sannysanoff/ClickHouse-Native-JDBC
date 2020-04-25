package com.github.housepower.jdbc.misc

/**
 * Copyright (C) 2018 SpectX
 * Created by Lauri Nõmme
 * 12.12.2018 16:11
 */
interface CheckedIterator<T, E : Throwable?> {
    suspend operator fun hasNext(): Boolean

    suspend operator fun next(): T
}