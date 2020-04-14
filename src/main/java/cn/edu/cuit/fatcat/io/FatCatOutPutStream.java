package cn.edu.cuit.fatcat.io;

import cn.edu.cuit.fatcat.adapter.ResponseAdapter;
import cn.edu.cuit.fatcat.message.Response;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;

/**
 * ServletOutPutStream实现类
 * @see javax.servlet.ServletOutputStream
 */
@Slf4j
public class FatCatOutPutStream extends ServletOutputStream {
    private static final String emptyChunkStr = "0\r\n\r\n";
    private static final byte[] emptyChunk = emptyChunkStr.getBytes();
    private static final String nextLineStr = "\r\n";
    private static final byte[] nextLine = nextLineStr.getBytes();

    /**
     * 用于输出的输出流
     */
    private OutputStream out;

    private Response response;

    /**
     * 缓冲区
     */
    private byte[] buffer;

    /**
     * 缓冲区大小
     */
    private int bufferSize;

    /**
     * 写入缓冲区的字节数
     */
    private int bufferCount;

    private boolean initBuffer = false;

    private boolean committed = false;

    public FatCatOutPutStream(OutputStream out, int bufferSize) {
        this.out = out;
        this.bufferCount = 0;
        this.bufferSize = bufferSize;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    /**
     * 如果没有打印状态行和响应头和空行, 就打印
     * @throws IOException
     */
    private void commit() throws IOException {
        if (!committed) {
            byte[] head = ResponseAdapter.INSTANCE.getResponseHead(response).getBytes(response.getCharset());
            out.write(head);
            out.flush();
            committed = true;
            response.setCommitted();
        }
    }

    /**
     * 如果没有初始化buffer，就初始化
     */
    private void initBuffer() {
        if (!initBuffer) {
            buffer = new byte[bufferSize];
            initBuffer = true;
        }
    }

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
        commit();
        initBuffer();
        if (s == null) {
            s = "null";
        }
        write(s.getBytes());
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
        commit();
        initBuffer();
        String value;
        if (b) {
            value = "true";
        } else {
            value = "false";
        }
        write(value.getBytes());
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
        commit();
        initBuffer();
        write(String.valueOf(c).getBytes());
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
        commit();
        initBuffer();
        write(String.valueOf(i).getBytes());
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
        commit();
        initBuffer();
        write(String.valueOf(l).getBytes());
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
        commit();
        initBuffer();
        write(String.valueOf(f).getBytes());
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
        commit();
        initBuffer();
        write(String.valueOf(d).getBytes());
    }

    /**
     * Writes a carriage return-line feed (CRLF)
     * to the client.
     *
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void println() throws IOException {
        commit();
        initBuffer();
        write(nextLine);
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
        print(s);
        println();
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
        print(b);
        println();
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
        print(c);
        println();
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
        print(i);
        println();
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
        print(l);
        println();
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
        print(f);
        println();
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
        print(d);
        println();
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
        // TODO: isReady
        return true;
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
        // TODO: setWriteListener

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
     * 这个意思是把b当作byte[32]的比特数组，只输出高8位，也就是byte[0] ~ byte[7],后24位忽略
     * 不明为什么要这样做
     *
     * @param b the <code>byte</code>.
     * @throws IOException if an I/O error occurs. In particular,
     *                     an <code>IOException</code> may be thrown if the
     *                     output stream has been closed.
     */
    @Override
    public void write(int b) throws IOException {
        commit();
        initBuffer();
        if (bufferCount >= buffer.length) {
            flush();
        }
        buffer[bufferCount++] = (byte)b;
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
        commit();
        initBuffer();
        write(b, 0, b.length);
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
        commit();
        initBuffer();
        if (len >= bufferSize) {
            // 如果待写的长度大于缓冲区大小，那么就把缓冲区的数据flush后直接提交
            flush();
            writeChunk(b, off, len);
            return;
        }
        if ((len + bufferCount) > bufferSize) {
            // 如果缓冲区不够写, 就先刷新缓冲区
            flush();
        }
        // 把数据写入buffer
        System.arraycopy(b, off, buffer, bufferCount, len);
        bufferCount += len;
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
        commit();
        initBuffer();
        if (bufferCount > 0) {
            writeChunk(buffer, 0, bufferCount);
            bufferCount = 0;
        }
    }

    /**
     * 块传输：边生成内容边传输
     * 首先打印响应行、响应头、空行
     * 再打印块，每个块分别是三个TCP帧：
     * 第一个TCP帧：{十六进制的块长度}\r\n（声明需要接收的块长度，以\r\n开始）
     * 第二个TCP帧：{块内容}（块内容，比特流）
     * 第三个TCP帧：\r\n（当接收长度够了，碰到\r\n结束符时，收完一个块）
     * 当服务器需要向浏览器告知已经传完时，需要传一个空块，这个块只需要一个TCP帧：
     * 0\r\n\r\n，都是ASCII码
     */
    private void writeChunk(byte [] buf, int off, int len) throws IOException {
        out.write((Integer.toHexString(len) + nextLineStr).getBytes());
        out.flush();
        out.write(buf, off, len);
        out.flush();
        out.write(nextLine);
        out.flush();
    }

    /**
     * 空块
     * 0\r\n\r\n
     * @throws IOException
     */
    public void writeEmptyChunk() throws IOException {
        out.write(emptyChunk);
        out.flush();
    }

    /**
     * Clears the content of the underlying buffer in the response without
     * clearing headers or status code. If the
     * response has been committed, this method throws an
     * <code>IllegalStateException</code>.
     *
     * @since Servlet 2.3
     */
    public void resetBuffer() {
        if (response.isCommitted()) {
            throw new IllegalStateException("Response has been committed !");
        }
        if (initBuffer) {
            bufferCount = 0;
        }
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
        out.close();
    }

    public void resetCommit() {
        this.committed = false;
    }
}
