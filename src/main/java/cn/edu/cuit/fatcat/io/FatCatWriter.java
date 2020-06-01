package cn.edu.cuit.fatcat.io;

import cn.edu.cuit.fatcat.message.Response;
import java.io.*;
import java.util.IllegalFormatException;
import java.util.Locale;

/**
 * 用输出流包装出来的writer类
 * 2020/4/14
 * @author fpc
 * @see javax.servlet.http.HttpServletResponse#getWriter
 */
public class FatCatWriter extends PrintWriter {
    private FatCatOutPutStream out;

    private Response response;

    /**
     * Creates a new PrintWriter, without automatic line flushing, from an
     * existing OutputStream.  This convenience constructor creates the
     * necessary intermediate OutputStreamWriter, which will convert characters
     * into bytes using the default character encoding.
     *
     * @param out An output stream
     * @see OutputStreamWriter#OutputStreamWriter(OutputStream)
     */
    public FatCatWriter(FatCatOutPutStream out) {
        super(out);
        this.out = out;
    }

    /**
     * Flushes the stream.
     *
     * @see #checkError()
     */
    @Override
    public void flush() {
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the stream and releases any system resources associated
     * with it. Closing a previously closed stream has no effect.
     *
     * @see #checkError()
     */
    @Override
    public void close() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a single character.
     *
     * @param c int specifying a character to be written.
     */
    @Override
    public void write(int c) {
        try {
            out.write(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes A Portion of an array of characters.
     *
     * @param buf Array of characters
     * @param off Offset from which to start writing characters
     * @param len Number of characters to write
     */
    @Override
    public void write(char[] buf, int off, int len) {
        try {
            out.print(new String(buf, off, len));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes an array of characters.  This method cannot be inherited from the
     * Writer class because it must suppress I/O exceptions.
     *
     * @param buf Array of characters to be written
     */
    @Override
    public void write(char[] buf) {
        write(buf, 0, buf.length);
    }

    /**
     * Writes a portion of a string.
     *
     * @param s   A String
     * @param off Offset from which to start writing characters
     * @param len Number of characters to write
     */
    @Override
    public void write(String s, int off, int len) {
        try {
            out.write(s.getBytes(response.getCharset()), off, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a string.  This method cannot be inherited from the Writer class
     * because it must suppress I/O exceptions.
     *
     * @param s String to be written
     */
    @Override
    public void write(String s) {
        write(s, 0, s.length());
    }

    /**
     * Prints a boolean value.  The string produced by <code>{@link
     * String#valueOf(boolean)}</code> is translated into bytes
     * according to the platform's default character encoding, and these bytes
     * are written in exactly the manner of the <code>{@link
     * #write(int)}</code> method.
     *
     * @param b The <code>boolean</code> to be printed
     */
    @Override
    public void print(boolean b) {
        write(String.valueOf(b));
    }

    /**
     * Prints a character.  The character is translated into one or more bytes
     * according to the platform's default character encoding, and these bytes
     * are written in exactly the manner of the <code>{@link
     * #write(int)}</code> method.
     *
     * @param c The <code>char</code> to be printed
     */
    @Override
    public void print(char c) {
        write(String.valueOf(c));
    }

    /**
     * Prints an integer.  The string produced by <code>{@link
     * String#valueOf(int)}</code> is translated into bytes according
     * to the platform's default character encoding, and these bytes are
     * written in exactly the manner of the <code>{@link #write(int)}</code>
     * method.
     *
     * @param i The <code>int</code> to be printed
     * @see Integer#toString(int)
     */
    @Override
    public void print(int i) {
        write(String.valueOf(i));
    }

    /**
     * Prints a long integer.  The string produced by <code>{@link
     * String#valueOf(long)}</code> is translated into bytes
     * according to the platform's default character encoding, and these bytes
     * are written in exactly the manner of the <code>{@link #write(int)}</code>
     * method.
     *
     * @param l The <code>long</code> to be printed
     * @see Long#toString(long)
     */
    @Override
    public void print(long l) {
        write(String.valueOf(l));
    }

    /**
     * Prints a floating-point number.  The string produced by <code>{@link
     * String#valueOf(float)}</code> is translated into bytes
     * according to the platform's default character encoding, and these bytes
     * are written in exactly the manner of the <code>{@link #write(int)}</code>
     * method.
     *
     * @param f The <code>float</code> to be printed
     * @see Float#toString(float)
     */
    @Override
    public void print(float f) {
        write(String.valueOf(f));
    }

    /**
     * Prints a double-precision floating-point number.  The string produced by
     * <code>{@link String#valueOf(double)}</code> is translated into
     * bytes according to the platform's default character encoding, and these
     * bytes are written in exactly the manner of the <code>{@link
     * #write(int)}</code> method.
     *
     * @param d The <code>double</code> to be printed
     * @see Double#toString(double)
     */
    @Override
    public void print(double d) {
        write(String.valueOf(d));
    }

    /**
     * Prints an array of characters.  The characters are converted into bytes
     * according to the platform's default character encoding, and these bytes
     * are written in exactly the manner of the <code>{@link #write(int)}</code>
     * method.
     *
     * @param s The array of chars to be printed
     * @throws NullPointerException If <code>s</code> is <code>null</code>
     */
    @Override
    public void print(char[] s) {
        write(s);
    }

    /**
     * Prints a string.  If the argument is <code>null</code> then the string
     * <code>"null"</code> is printed.  Otherwise, the string's characters are
     * converted into bytes according to the platform's default character
     * encoding, and these bytes are written in exactly the manner of the
     * <code>{@link #write(int)}</code> method.
     *
     * @param s The <code>String</code> to be printed
     */
    @Override
    public void print(String s) {
        write(s);
    }

    /**
     * Prints an object.  The string produced by the <code>{@link
     * String#valueOf(Object)}</code> method is translated into bytes
     * according to the platform's default character encoding, and these bytes
     * are written in exactly the manner of the <code>{@link #write(int)}</code>
     * method.
     *
     * @param obj The <code>Object</code> to be printed
     * @see Object#toString()
     */
    @Override
    public void print(Object obj) {
        write(String.valueOf(obj));
    }

    /**
     * Terminates the current line by writing the line separator string.  The
     * line separator string is defined by the system property
     * <code>line.separator</code>, and is not necessarily a single newline
     * character (<code>'\n'</code>).
     */
    @Override
    public void println() {
        try {
            out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a boolean value and then terminates the line.  This method behaves
     * as though it invokes <code>{@link #print(boolean)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x the <code>boolean</code> value to be printed
     */
    @Override
    public void println(boolean x) {
        try {
            out.println(x);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a character and then terminates the line.  This method behaves as
     * though it invokes <code>{@link #print(char)}</code> and then <code>{@link
     * #println()}</code>.
     *
     * @param x the <code>char</code> value to be printed
     */
    @Override
    public void println(char x) {
        try {
            out.println(x);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints an integer and then terminates the line.  This method behaves as
     * though it invokes <code>{@link #print(int)}</code> and then <code>{@link
     * #println()}</code>.
     *
     * @param x the <code>int</code> value to be printed
     */
    @Override
    public void println(int x) {
        try {
            out.println(x);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a long integer and then terminates the line.  This method behaves
     * as though it invokes <code>{@link #print(long)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x the <code>long</code> value to be printed
     */
    @Override
    public void println(long x) {
        try {
            out.println(x);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a floating-point number and then terminates the line.  This method
     * behaves as though it invokes <code>{@link #print(float)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x the <code>float</code> value to be printed
     */
    @Override
    public void println(float x) {
        try {
            out.println(x);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a double-precision floating-point number and then terminates the
     * line.  This method behaves as though it invokes <code>{@link
     * #print(double)}</code> and then <code>{@link #println()}</code>.
     *
     * @param x the <code>double</code> value to be printed
     */
    @Override
    public void println(double x) {
        try {
            out.println(x);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints an array of characters and then terminates the line.  This method
     * behaves as though it invokes <code>{@link #print(char[])}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x the array of <code>char</code> values to be printed
     */
    @Override
    public void println(char[] x) {
        try {
            out.println(new String(x));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a String and then terminates the line.  This method behaves as
     * though it invokes <code>{@link #print(String)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x the <code>String</code> value to be printed
     */
    @Override
    public void println(String x) {
        try {
            out.println(x);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints an Object and then terminates the line.  This method calls
     * at first String.valueOf(x) to get the printed object's string value,
     * then behaves as
     * though it invokes <code>{@link #print(String)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x The <code>Object</code> to be printed.
     */
    @Override
    public void println(Object x) {
        try {
            out.println(String.valueOf(x));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A convenience method to write a formatted string to this writer using
     * the specified format string and arguments.  If automatic flushing is
     * enabled, calls to this method will flush the output buffer.
     *
     * <p> An invocation of this method of the form <tt>out.printf(format,
     * args)</tt> behaves in exactly the same way as the invocation
     *
     * <pre>
     *     out.format(format, args) </pre>
     *
     * @param format A format string as described in <a
     *               href="../util/Formatter.html#syntax">Format string syntax</a>.
     * @param args   Arguments referenced by the format specifiers in the format
     *               string.  If there are more arguments than format specifiers, the
     *               extra arguments are ignored.  The number of arguments is
     *               variable and may be zero.  The maximum number of arguments is
     *               limited by the maximum dimension of a Java array as defined by
     *               <cite>The Java&trade; Virtual Machine Specification</cite>.
     *               The behaviour on a
     *               <tt>null</tt> argument depends on the <a
     *               href="../util/Formatter.html#syntax">conversion</a>.
     * @return This writer
     * @throws IllegalFormatException If a format string contains an illegal syntax, a format
     *                                specifier that is incompatible with the given arguments,
     *                                insufficient arguments given the format string, or other
     *                                illegal conditions.  For specification of all possible
     *                                formatting errors, see the <a
     *                                href="../util/Formatter.html#detail">Details</a> section of the
     *                                formatter class specification.
     * @throws NullPointerException   If the <tt>format</tt> is <tt>null</tt>
     * @since 1.5
     */
    @Override
    public PrintWriter printf(String format, Object... args) {
       write(String.format(format, args));
        return this;
    }

    /**
     * A convenience method to write a formatted string to this writer using
     * the specified format string and arguments.  If automatic flushing is
     * enabled, calls to this method will flush the output buffer.
     *
     * <p> An invocation of this method of the form <tt>out.printf(l, format,
     * args)</tt> behaves in exactly the same way as the invocation
     *
     * <pre>
     *     out.format(l, format, args) </pre>
     *
     * @param l      The {@linkplain Locale locale} to apply during
     *               formatting.  If <tt>l</tt> is <tt>null</tt> then no localization
     *               is applied.
     * @param format A format string as described in <a
     *               href="../util/Formatter.html#syntax">Format string syntax</a>.
     * @param args   Arguments referenced by the format specifiers in the format
     *               string.  If there are more arguments than format specifiers, the
     *               extra arguments are ignored.  The number of arguments is
     *               variable and may be zero.  The maximum number of arguments is
     *               limited by the maximum dimension of a Java array as defined by
     *               <cite>The Java&trade; Virtual Machine Specification</cite>.
     *               The behaviour on a
     *               <tt>null</tt> argument depends on the <a
     *               href="../util/Formatter.html#syntax">conversion</a>.
     * @return This writer
     * @throws IllegalFormatException If a format string contains an illegal syntax, a format
     *                                specifier that is incompatible with the given arguments,
     *                                insufficient arguments given the format string, or other
     *                                illegal conditions.  For specification of all possible
     *                                formatting errors, see the <a
     *                                href="../util/Formatter.html#detail">Details</a> section of the
     *                                formatter class specification.
     * @throws NullPointerException   If the <tt>format</tt> is <tt>null</tt>
     * @since 1.5
     */
    @Override
    public PrintWriter printf(Locale l, String format, Object... args) {
        write(String.format(l, format, args));
        return this;
    }

    /**
     * Writes a formatted string to this writer using the specified format
     * string and arguments.  If automatic flushing is enabled, calls to this
     * method will flush the output buffer.
     *
     * <p> The locale always used is the one returned by {@link
     * Locale#getDefault() Locale.getDefault()}, regardless of any
     * previous invocations of other formatting methods on this object.
     *
     * @param format A format string as described in <a
     *               href="../util/Formatter.html#syntax">Format string syntax</a>.
     * @param args   Arguments referenced by the format specifiers in the format
     *               string.  If there are more arguments than format specifiers, the
     *               extra arguments are ignored.  The number of arguments is
     *               variable and may be zero.  The maximum number of arguments is
     *               limited by the maximum dimension of a Java array as defined by
     *               <cite>The Java&trade; Virtual Machine Specification</cite>.
     *               The behaviour on a
     *               <tt>null</tt> argument depends on the <a
     *               href="../util/Formatter.html#syntax">conversion</a>.
     * @return This writer
     * @throws IllegalFormatException If a format string contains an illegal syntax, a format
     *                                specifier that is incompatible with the given arguments,
     *                                insufficient arguments given the format string, or other
     *                                illegal conditions.  For specification of all possible
     *                                formatting errors, see the <a
     *                                href="../util/Formatter.html#detail">Details</a> section of the
     *                                Formatter class specification.
     * @throws NullPointerException   If the <tt>format</tt> is <tt>null</tt>
     * @since 1.5
     */
    @Override
    public PrintWriter format(String format, Object... args) {
        write(String.format(format, args));
        return this;
    }

    /**
     * Writes a formatted string to this writer using the specified format
     * string and arguments.  If automatic flushing is enabled, calls to this
     * method will flush the output buffer.
     *
     * @param l      The {@linkplain Locale locale} to apply during
     *               formatting.  If <tt>l</tt> is <tt>null</tt> then no localization
     *               is applied.
     * @param format A format string as described in <a
     *               href="../util/Formatter.html#syntax">Format string syntax</a>.
     * @param args   Arguments referenced by the format specifiers in the format
     *               string.  If there are more arguments than format specifiers, the
     *               extra arguments are ignored.  The number of arguments is
     *               variable and may be zero.  The maximum number of arguments is
     *               limited by the maximum dimension of a Java array as defined by
     *               <cite>The Java&trade; Virtual Machine Specification</cite>.
     *               The behaviour on a
     *               <tt>null</tt> argument depends on the <a
     *               href="../util/Formatter.html#syntax">conversion</a>.
     * @return This writer
     * @throws IllegalFormatException If a format string contains an illegal syntax, a format
     *                                specifier that is incompatible with the given arguments,
     *                                insufficient arguments given the format string, or other
     *                                illegal conditions.  For specification of all possible
     *                                formatting errors, see the <a
     *                                href="../util/Formatter.html#detail">Details</a> section of the
     *                                formatter class specification.
     * @throws NullPointerException   If the <tt>format</tt> is <tt>null</tt>
     * @since 1.5
     */
    @Override
    public PrintWriter format(Locale l, String format, Object... args) {
        write(String.format(l, format, args));
        return this;
    }

    /**
     * Appends the specified character sequence to this writer.
     *
     * <p> An invocation of this method of the form <tt>out.append(csq)</tt>
     * behaves in exactly the same way as the invocation
     *
     * <pre>
     *     out.write(csq.toString()) </pre>
     *
     * <p> Depending on the specification of <tt>toString</tt> for the
     * character sequence <tt>csq</tt>, the entire sequence may not be
     * appended. For instance, invoking the <tt>toString</tt> method of a
     * character buffer will return a subsequence whose content depends upon
     * the buffer's position and limit.
     *
     * @param csq The character sequence to append.  If <tt>csq</tt> is
     *            <tt>null</tt>, then the four characters <tt>"null"</tt> are
     *            appended to this writer.
     * @return This writer
     * @since 1.5
     */
    @Override
    public PrintWriter append(CharSequence csq) {
        write(csq.toString());
        return this;
    }

    /**
     * Appends a subsequence of the specified character sequence to this writer.
     *
     * <p> An invocation of this method of the form <tt>out.append(csq, start,
     * end)</tt> when <tt>csq</tt> is not <tt>null</tt>, behaves in
     * exactly the same way as the invocation
     *
     * <pre>
     *     out.write(csq.subSequence(start, end).toString()) </pre>
     *
     * @param csq   The character sequence from which a subsequence will be
     *              appended.  If <tt>csq</tt> is <tt>null</tt>, then characters
     *              will be appended as if <tt>csq</tt> contained the four
     *              characters <tt>"null"</tt>.
     * @param start The index of the first character in the subsequence
     * @param end   The index of the character following the last character in the
     *              subsequence
     * @return This writer
     * @throws IndexOutOfBoundsException If <tt>start</tt> or <tt>end</tt> are negative, <tt>start</tt>
     *                                   is greater than <tt>end</tt>, or <tt>end</tt> is greater than
     *                                   <tt>csq.length()</tt>
     * @since 1.5
     */
    @Override
    public PrintWriter append(CharSequence csq, int start, int end) {
        write(csq.subSequence(start, end).toString());
        return this;
    }

    /**
     * Appends the specified character to this writer.
     *
     * <p> An invocation of this method of the form <tt>out.append(c)</tt>
     * behaves in exactly the same way as the invocation
     *
     * <pre>
     *     out.write(c) </pre>
     *
     * @param c The 16-bit character to append
     * @return This writer
     * @since 1.5
     */
    @Override
    public PrintWriter append(char c) {
        write(c);
        return this;
    }

    /**
     * 刷新缓冲区
     */
    public void flushBuffer() throws IOException {
        out.flush();
    }

    /**
     * 重置缓冲区
     */
    public void resetBuffer() {
        out.resetBuffer();
    }

    /**
     * 设置响应对象
     */
    public void setResponse(Response response) {
        this.response = response;
    }

    /**
     * 输出空块
     */
    public void writeEmptyChunk() throws IOException {
        out.writeEmptyChunk();
    }
}
