package com.github.housepower.jdbc.misc

import java.util.*
import java.util.function.Consumer

/**
 * Slice Object just like the slice of Go
 */
class Slice : Iterable<Any?> {
    private var array: Array<Any?>
    private var capacity: Int
    private var offset: Int
    private var pos: Int

    constructor(capacity: Int) {
        offset = 0
        pos = 0
        array = arrayOfNulls(capacity)
        this.capacity = capacity
    }

    constructor(array: Array<Any?>) {
        offset = 0
        pos = array.size
        this.array = array
        capacity = array.size
    }

    constructor(slice: Slice, offset: Int, pos: Int) {
        array = slice.array
        capacity = slice.capacity
        this.offset = offset
        this.pos = pos
    }

    fun size(): Int {
        return pos - offset
    }

    fun sub(offsetAdd: Int, posAdd: Int): Slice {
        return Slice(this, offset + offsetAdd, offset + posAdd)
    }

    operator fun get(index: Int): Any? {
        return array[index + offset]
    }

    fun add(`object`: Any?) {
        if (pos >= capacity) {
            grow(capacity)
        }
        array[pos] = `object`
        pos++
    }

    operator fun set(index: Int, `object`: Any?) {
        array[offset + index] = `object`
    }

    private fun grow(minCapacity: Int) {
        // overflow-conscious code
        val oldCapacity = array.size
        var newCapacity = oldCapacity + (oldCapacity shr 1)
        if (newCapacity - minCapacity < 0) newCapacity = minCapacity
        if (newCapacity - MAX_ARRAY_SIZE > 0) newCapacity = hugeCapacity(minCapacity)
        // minCapacity is usually close to size, so this is a win:
        array = Arrays.copyOf(array, newCapacity)
        capacity = newCapacity
    }

    class SliceIterator internal constructor(var slice: Slice) : MutableIterator<Any?> {
        var current: Int
        override fun hasNext(): Boolean {
            return current < slice.pos
        }

        override fun next(): Any? {
            val obj = slice.array[current]
            current++
            return obj
        }

        override fun remove() {
            return
        }

//        fun forEachRemaining(action: Consumer<Any>) {
//            return
//        }

        init {
            current = slice.offset
        }
    }

    override fun iterator(): MutableIterator<Any?> {
        return SliceIterator(this)
    }

    override fun forEach(action: Consumer<in Any?>) {}

    override fun spliterator(): Spliterator<Any?> {
        return null!!
    }

    companion object {
        private const val MAX_ARRAY_SIZE = Int.MAX_VALUE - 8
        private fun hugeCapacity(minCapacity: Int): Int {
            if (minCapacity < 0) throw OutOfMemoryError()
            return if (minCapacity > MAX_ARRAY_SIZE) Int.MAX_VALUE else MAX_ARRAY_SIZE
        }
    }
}