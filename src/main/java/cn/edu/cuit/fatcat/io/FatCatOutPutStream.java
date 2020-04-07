package cn.edu.cuit.fatcat.io;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;

public class FatCatOutPutStream extends ServletOutputStream {
    /**
     * Writes a <code>String</code> to the client,
     * without a carriage return-line feed (CRLF)
     * character at the end.
     *
     * @param s the <code>String</code> to send to the client
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void print(String s) throws IOException {
        super.print(s);
    }

    /**
     * Writes a <code>boolean</code> value to the client,
     * with no carriage return-line feed (CRLF)
     * character at the end.
     *
     * @param b the <code>boolean</code> value
     *          to send to the client
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void print(boolean b) throws IOException {
        super.print(b);
    }

    /**
     * Writes a character to the client,
     * with no carriage return-line feed (CRLF)
     * at the end.
     *
     * @param c the character to send to the client
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void print(char c) throws IOException {
        super.print(c);
    }

    /**
     * Writes an int to the client,
     * with no carriage return-line feed (CRLF)
     * at the end.
     *
     * @param i the int to send to the client
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void print(int i) throws IOException {
        super.print(i);
    }

    /**
     * Writes a <code>long</code> value to the client,
     * with no carriage return-line feed (CRLF) at the end.
     *
     * @param l the <code>long</code> value
     *          to send to the client
     * @throws IOException if an input or output exception
     *                     occurred
     */
    @Override
    public void print(long l) throws IOException {
        super.print(l);
    }

    /**
     * Writes a <code>float</code> value to the client,
     * with no carriage return-line feed (CRLF) at the end.
     *
     * @param f the <code>float</code> value
     *          to send to the client
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void print(float f) throws IOException {
        super.print(f);
    }

    /**
     * Writes a <code>double</code> value to the client,
     * with no carriage return-line feed (CRLF) at the end.
     *
     * @param d the <code>double</code> value
     *          to send to the client
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void print(double d) throws IOException {
        super.print(d);
    }

    /**
     * Writes a carriage return-line feed (CRLF)
     * to the client.
     *
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void println() throws IOException {
        super.println();
    }

    /**
     * Writes a <code>String</code> to the client,
     * followed by a carriage return-line feed (CRLF).
     *
     * @param s the <code>String</code> to write to the client
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void println(String s) throws IOException {
        super.println(s);
    }

    /**
     * Writes a <code>boolean</code> value to the client,
     * followed by a
     * carriage return-line feed (CRLF).
     *
     * @param b the <code>boolean</code> value
     *          to write to the client
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void println(boolean b) throws IOException {
        super.println(b);
    }

    /**
     * Writes a character to the client, followed by a carriage
     * return-line feed (CRLF).
     *
     * @param c the character to write to the client
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void println(char c) throws IOException {
        super.println(c);
    }

    /**
     * Writes an int to the client, followed by a
     * carriage return-line feed (CRLF) character.
     *
     * @param i the int to write to the client
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void println(int i) throws IOException {
        super.println(i);
    }

    /**
     * Writes a <code>long</code> value to the client, followed by a
     * carriage return-line feed (CRLF).
     *
     * @param l the <code>long</code> value to write to the client
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void println(long l) throws IOException {
        super.println(l);
    }

    /**
     * Writes a <code>float</code> value to the client,
     * followed by a carriage return-line feed (CRLF).
     *
     * @param f the <code>float</code> value
     *          to write to the client
     * @throws IOException if an input or output exception
     *                     occurred
     */
    @Override
    public void println(float f) throws IOException {
        super.println(f);
    }

    /**
     * Writes a <code>double</code> value to the client,
     * followed by a carriage return-line feed (CRLF).
     *
     * @param d the <code>double</code> value
     *          to write to the client
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void println(double d) throws IOException {
        super.println(d);
    }

    /**
     * This method can be used to determine if data can be written without blocking.
     *
     * @return <code>true</code> if a write to this <code>ServletOutputStream</code>
     * will succeed, otherwise returns <code>false</code>.
     * @since Servlet 3.1
     */
    @Override
    public boolean isReady() {
        return false;
    }

    /**
     * Instructs the <code>ServletOutputStream</code> to invoke the provided
     * {@link WriteListener} when it is possible to write
     *
     * @param writeListener the {@link WriteListener} that should be notified
     *                      when it's possible to write
     * @throws IllegalStateException if one of the following conditions is true
     *                               <ul>
     *                               <li>the associated request is neither upgraded nor the async started
     *                               <li>setWriteListener is called more than once within the scope of the same request.
     *                               </ul>
     * @throws NullPointerException  if writeListener is null
     * @since Servlet 3.1
     */
    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    /**
     * Writes the specified byte to this output stream. The general
     * contract for <code>write</code> is that one byte is written
     * to the output stream. The byte to be written is the eight
     * low-order bits of the argument <code>b</code>. The 24
     * high-order bits of <code>b</code> are ignored.
     * <p>
     * Subclasses of <code>OutputStream</code> must provide an
     * implementation for this method.
     *
     * @param b the <code>byte</code>.
     * @throws IOException if an I/O error occurs. In particular,
     *                     an <code>IOException</code> may be thrown if the
     *                     output stream has been closed.
     */
    @Override
    public void write(int b) throws IOException {

    }

    /**
     * Writes <code>b.length</code> bytes from the specified byte array
     * to this output stream. The general contract for <code>write(b)</code>
     * is that it should have exactly the same effect as the call
     * <code>write(b, 0, b.length)</code>.
     *
     * @param b the data.
     * @throws IOException if an I/O error occurs.
     * @see OutputStream#write(byte[], int, int)
     */
    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array
     * starting at offset <code>off</code> to this output stream.
     * The general contract for <code>write(b, off, len)</code> is that
     * some of the bytes in the array <code>b</code> are written to the
     * output stream in order; element <code>b[off]</code> is the first
     * byte written and <code>b[off+len-1]</code> is the last byte written
     * by this operation.
     * <p>
     * The <code>write</code> method of <code>OutputStream</code> calls
     * the write method of one argument on each of the bytes to be
     * written out. Subclasses are encouraged to override this method and
     * provide a more efficient implementation.
     * <p>
     * If <code>b</code> is <code>null</code>, a
     * <code>NullPointerException</code> is thrown.
     * <p>
     * If <code>off</code> is negative, or <code>len</code> is negative, or
     * <code>off+len</code> is greater than the length of the array
     * <code>b</code>, then an <tt>IndexOutOfBoundsException</tt> is thrown.
     *
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws IOException if an I/O error occurs. In particular,
     *                     an <code>IOException</code> is thrown if the output
     *                     stream is closed.
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
    }

    /**
     * Flushes this output stream and forces any buffered output bytes
     * to be written out. The general contract of <code>flush</code> is
     * that calling it is an indication that, if any bytes previously
     * written have been buffered by the implementation of the output
     * stream, such bytes should immediately be written to their
     * intended destination.
     * <p>
     * If the intended destination of this stream is an abstraction provided by
     * the underlying operating system, for example a file, then flushing the
     * stream guarantees only that bytes previously written to the stream are
     * passed to the operating system for writing; it does not guarantee that
     * they are actually written to a physical device such as a disk drive.
     * <p>
     * The <code>flush</code> method of <code>OutputStream</code> does nothing.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void flush() throws IOException {
        super.flush();
    }

    /**
     * Closes this output stream and releases any system resources
     * associated with this stream. The general contract of <code>close</code>
     * is that it closes the output stream. A closed stream cannot perform
     * output operations and cannot be reopened.
     * <p>
     * The <code>close</code> method of <code>OutputStream</code> does nothing.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        super.close();
    }
}
