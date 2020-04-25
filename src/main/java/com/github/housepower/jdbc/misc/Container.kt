package com.github.housepower.jdbc.misc

class Container<T>(private val left: T, private val right: T) {
    private var isLeft = true
    fun select(isRight: Boolean) {
        isLeft = !isRight
    }

    fun get(): T {
        return if (isLeft) left else right
    }

}