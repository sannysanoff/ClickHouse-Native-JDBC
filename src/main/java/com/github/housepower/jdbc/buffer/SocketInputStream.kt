package com.github.housepower.jdbc.buffer

import kotlinx.sockets.Socket
import java.nio.ByteBuffer
import java.util.*


class SocketInputStream(val sock: Socket) {

    val bb = ByteBuffer.allocate(1)

    suspend fun read() : Int {
        val rd = sock.read(bb);
        if (rd < 1)
            return rd;
        return bb.get(0).toInt();
    }


    suspend open fun read(b: ByteArray): Int {
        val bb0 = ByteBuffer.wrap(b)
        return sock.read(bb0)
    }


    suspend open fun read(b: ByteArray, off: Int, len: Int): Int {
        val bb0 = ByteBuffer.wrap(b, off, len)
        return sock.read(bb0)
//        Objects.checkFromIndexSize(off, len, b.size)
//        if (len == 0) {
//            return 0
//        }
//        var c: Int = read()
//        if (c == -1) {
//            return -1
//        }
//        b.get(off) = c.toByte()
//        var i: Int = 1
//        try {
//            while (i < len) {
//                c = read()
//                if (c == -1) {
//                    break
//                }
//                b.get(off + i) = c.toByte()
//                i++
//            }
//        } catch (ee: IOException) {
//        }
//        return i
    }

    /**
     * Reads all remaining bytes from the input stream. This method blocks until
     * all remaining bytes have been read and end of stream is detected, or an
     * exception is thrown. This method does not close the input stream.
     *
     *
     *  When this stream reaches end of stream, further invocations of this
     * method will return an empty byte array.
     *
     *
     *  Note that this method is intended for simple cases where it is
     * convenient to read all bytes into a byte array. It is not intended for
     * reading input streams with large amounts of data.
     *
     *
     *  The behavior for the case where the input stream is *asynchronously
     * closed*, or the thread interrupted during the read, is highly input
     * stream specific, and therefore not specified.
     *
     *
     *  If an I/O error occurs reading from the input stream, then it may do
     * so after some, but not all, bytes have been read. Consequently the input
     * stream may not be at end of stream and may be in an inconsistent state.
     * It is strongly recommended that the stream be promptly closed if an I/O
     * error occurs.
     *
     * @implSpec
     * This method invokes [.readNBytes] with a length of
     * [Integer.MAX_VALUE].
     *
     * @return a byte array containing the bytes read from this input stream
     * @throws IOException if an I/O error occurs
     * @throws OutOfMemoryError if an array of the required size cannot be
     * allocated.
     *
     * @since 9
     */

//    open fun skip(n: Long): Long {
//        var remaining: Long = n
//        var nr: Int
//        if (n <= 0) {
//            return 0
//        }
//        val size: Int = Math.min(InputStream.Companion.MAX_SKIP_BUFFER_SIZE.toLong(), remaining).toInt()
//        val skipBuffer: ByteArray = ByteArray(size)
//        while (remaining > 0) {
//            nr = read(skipBuffer, 0, Math.min(size.toLong(), remaining).toInt())
//            if (nr < 0) {
//                break
//            }
//            remaining -= nr.toLong()
//        }
//        return n - remaining
//    }
//
    /**
     * Skips over and discards exactly `n` bytes of data from this input
     * stream.  If `n` is zero, then no bytes are skipped.
     * If `n` is negative, then no bytes are skipped.
     * Subclasses may handle the negative value differently.
     *
     *
     *  This method blocks until the requested number of bytes have been
     * skipped, end of file is reached, or an exception is thrown.
     *
     *
     *  If end of stream is reached before the stream is at the desired
     * position, then an `EOFException` is thrown.
     *
     *
     *  If an I/O error occurs, then the input stream may be
     * in an inconsistent state. It is strongly recommended that the
     * stream be promptly closed if an I/O error occurs.
     *
     * @implNote
     * Subclasses are encouraged to provide a more efficient implementation
     * of this method.
     *
     * @implSpec
     * If `n` is zero or negative, then no bytes are skipped.
     * If `n` is positive, the default implementation of this method
     * invokes [skip()][.skip] with parameter `n`.  If the
     * return value of `skip(n)` is non-negative and less than `n`,
     * then [.read] is invoked repeatedly until the stream is `n`
     * bytes beyond its position when this method was invoked or end of stream
     * is reached.  If the return value of `skip(n)` is negative or
     * greater than `n`, then an `IOException` is thrown.  Any
     * exception thrown by `skip()` or `read()` will be propagated.
     *
     * @param      n   the number of bytes to be skipped.
     * @throws     EOFException if end of stream is encountered before the
     * stream can be positioned `n` bytes beyond its position
     * when this method was invoked.
     * @throws     IOException  if the stream cannot be positioned properly or
     * if an I/O error occurs.
     * @see java.io.InputStream.skip
     */

//    open fun skipNBytes(n: Long) {
//        var n: Long = n
//        if (n > 0) {
//            val ns: Long = skip(n)
//            if (ns >= 0 && ns < n) { // skipped too few bytes
//                // adjust number to skip
//                n -= ns
//                // read until requested number skipped or EOS reached
//                while (n > 0 && read() != -1) {
//                    n--
//                }
//                // if not enough skipped, then EOFE
//                if (n != 0L) {
//                    throw EOFException()
//                }
//            } else if (ns != n) { // skipped negative or too many bytes
//                throw IOException("Unable to skip exactly")
//            }
//        }
//    }

    /**
     * Returns an estimate of the number of bytes that can be read (or skipped
     * over) from this input stream without blocking, which may be 0, or 0 when
     * end of stream is detected.  The read might be on the same thread or
     * another thread.  A single read or skip of this many bytes will not block,
     * but may read or skip fewer bytes.
     *
     *
     *  Note that while some implementations of `InputStream` will
     * return the total number of bytes in the stream, many will not.  It is
     * never correct to use the return value of this method to allocate
     * a buffer intended to hold all data in this stream.
     *
     *
     *  A subclass's implementation of this method may choose to throw an
     * [IOException] if this input stream has been closed by invoking the
     * [.close] method.
     *
     *
     *  The `available` method of `InputStream` always returns
     * `0`.
     *
     *
     *  This method should be overridden by subclasses.
     *
     * @return     an estimate of the number of bytes that can be read (or
     * skipped over) from this input stream without blocking or
     * `0` when it reaches the end of the input stream.
     * @throws     IOException if an I/O error occurs.
     */

    /**
     * Closes this input stream and releases any system resources associated
     * with the stream.
     *
     *
     *  The `close` method of `InputStream` does
     * nothing.
     *
     * @throws     IOException  if an I/O error occurs.
     */

    suspend fun close() {
    }

    /**
     * Marks the current position in this input stream. A subsequent call to
     * the `reset` method repositions this stream at the last marked
     * position so that subsequent reads re-read the same bytes.
     *
     *
     *  The `readlimit` arguments tells this input stream to
     * allow that many bytes to be read before the mark position gets
     * invalidated.
     *
     *
     *  The general contract of `mark` is that, if the method
     * `markSupported` returns `true`, the stream somehow
     * remembers all the bytes read after the call to `mark` and
     * stands ready to supply those same bytes again if and whenever the method
     * `reset` is called.  However, the stream is not required to
     * remember any data at all if more than `readlimit` bytes are
     * read from the stream before `reset` is called.
     *
     *
     *  Marking a closed stream should not have any effect on the stream.
     *
     *
     *  The `mark` method of `InputStream` does
     * nothing.
     *
     * @param   readlimit   the maximum limit of bytes that can be read before
     * the mark position becomes invalid.
     * @see java.io.InputStream.reset
     */


}