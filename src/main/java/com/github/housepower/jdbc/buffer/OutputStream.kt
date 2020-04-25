package com.github.housepower.jdbc.buffer

import java.io.IOException
import java.util.*


/**
 * This abstract class is the superclass of all classes representing
 * an output stream of bytes. An output stream accepts output bytes
 * and sends them to some sink.
 *
 *
 * Applications that need to define a subclass of
 * `OutputStream` must always provide at least a method
 * that writes one byte of output.
 *
 * @author  Arthur van Hoff
 * @see java.io.BufferedOutputStream
 *
 * @see java.io.ByteArrayOutputStream
 *
 * @see java.io.DataOutputStream
 *
 * @see java.io.FilterOutputStream
 *
 * @see java.io.InputStream
 *
 * @see java.io.OutputStream.write
 * @since   1.0
 */
abstract class OutputStream
/**
 * Constructor for subclasses to call.
 */
     {
    /**
     * Writes the specified byte to this output stream. The general
     * contract for `write` is that one byte is written
     * to the output stream. The byte to be written is the eight
     * low-order bits of the argument `b`. The 24
     * high-order bits of `b` are ignored.
     *
     *
     * Subclasses of `OutputStream` must provide an
     * implementation for this method.
     *
     * @param      b   the `byte`.
     * @throws     IOException  if an I/O error occurs. In particular,
     * an `IOException` may be thrown if the
     * output stream has been closed.
     */
    
    abstract suspend fun write(b: Int)

    /**
     * Writes `b.length` bytes from the specified byte array
     * to this output stream. The general contract for `write(b)`
     * is that it should have exactly the same effect as the call
     * `write(b, 0, b.length)`.
     *
     * @param      b   the data.
     * @throws     IOException  if an I/O error occurs.
     * @see java.io.OutputStream.write
     */
    
    open suspend fun write(b: ByteArray) {
        write(b, 0, b.size)
    }

    /**
     * Writes `len` bytes from the specified byte array
     * starting at offset `off` to this output stream.
     * The general contract for `write(b, off, len)` is that
     * some of the bytes in the array `b` are written to the
     * output stream in order; element `b[off]` is the first
     * byte written and `b[off+len-1]` is the last byte written
     * by this operation.
     *
     *
     * The `write` method of `OutputStream` calls
     * the write method of one argument on each of the bytes to be
     * written out. Subclasses are encouraged to override this method and
     * provide a more efficient implementation.
     *
     *
     * If `b` is `null`, a
     * `NullPointerException` is thrown.
     *
     *
     * If `off` is negative, or `len` is negative, or
     * `off+len` is greater than the length of the array
     * `b`, then an `IndexOutOfBoundsException` is thrown.
     *
     * @param      b     the data.
     * @param      off   the start offset in the data.
     * @param      len   the number of bytes to write.
     * @throws     IOException  if an I/O error occurs. In particular,
     * an `IOException` is thrown if the output
     * stream is closed.
     */
    
    open suspend fun write(b: ByteArray, off: Int, len: Int) {
        Objects.checkFromIndexSize(off, len, b.size)
        // len == 0 condition implicitly handled by loop bounds
        for (i in 0 until len) {
            val a = b[off + i]
            write(a.toInt())
        }
    }

    /**
     * Flushes this output stream and forces any buffered output bytes
     * to be written out. The general contract of `flush` is
     * that calling it is an indication that, if any bytes previously
     * written have been buffered by the implementation of the output
     * stream, such bytes should immediately be written to their
     * intended destination.
     *
     *
     * If the intended destination of this stream is an abstraction provided by
     * the underlying operating system, for example a file, then flushing the
     * stream guarantees only that bytes previously written to the stream are
     * passed to the operating system for writing; it does not guarantee that
     * they are actually written to a physical device such as a disk drive.
     *
     *
     * The `flush` method of `OutputStream` does nothing.
     *
     * @throws     IOException  if an I/O error occurs.
     */
    
    open suspend fun flush() {
    }

    /**
     * Closes this output stream and releases any system resources
     * associated with this stream. The general contract of `close`
     * is that it closes the output stream. A closed stream cannot perform
     * output operations and cannot be reopened.
     *
     *
     * The `close` method of `OutputStream` does nothing.
     *
     * @throws     IOException  if an I/O error occurs.
     */
    
    open suspend fun close() {
    }

}
