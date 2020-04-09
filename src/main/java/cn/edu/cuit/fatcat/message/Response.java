package cn.edu.cuit.fatcat.message;

import cn.edu.cuit.fatcat.RecycleAble;
import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.fatcat.io.FatCatOutPutStream;
import cn.edu.cuit.fatcat.io.FatCatWriter;
import cn.edu.cuit.fatcat.io.SocketWrapper;
import cn.edu.cuit.fatcat.util.FastHttpDateFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 响应报文实体
 *
 * @author fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response implements HttpServletResponse, RecycleAble {
    private String protocol;

    private Integer code;

    private String status;

    private Locale locale;

    private Boolean committed;

    private Integer bufferSize;

    private String characterEncoding;

    private Map<String, List<String>> headers; // TODO: Content-Length, Data, Server

    private List<Cookie> cookies;

    private Request request; // 对应的request对象

    private SocketWrapper socketWrapper;

    private Boolean useWriter; // 如果使用writer，就不能使用OutputStream

    private PrintWriter printWriter;

    private Boolean useStream; // 如果使用stream，就不能使用writer

    private OutputStream outputStream;

    /**
     * Adds the specified cookie to the response.  This method can be called
     * multiple times to set more than one cookie.
     *
     * @param cookie the Cookie to return to the client
     */
    @Override
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    /**
     * Returns a boolean indicating whether the named response header
     * has already been set.
     *
     * @param    name    the header name
     * @return        <code>true</code> if the named response header
     * has already been set;
     * <code>false</code> otherwise
     */
    @Override
    public boolean containsHeader(String name) {
        return headers.containsKey(name);
    }

    /**
     * Encodes the specified URL by including the session ID,
     * or, if encoding is not needed, returns the URL unchanged.
     * The implementation of this method includes the logic to
     * determine whether the session ID needs to be encoded in the URL.
     * For example, if the browser supports cookies, or session
     * tracking is turned off, URL encoding is unnecessary.
     *
     * <p>For robust session tracking, all URLs emitted by a servlet
     * should be run through this
     * method.  Otherwise, URL rewriting cannot be used with browsers
     * which do not support cookies.
     *
     * <p>If the URL is relative, it is always relative to the current
     * HttpServletRequest.
     *
     * @throws IllegalArgumentException if the url is not valid
     * @param    url    the url to be encoded.
     * @return the encoded URL if encoding is needed;
     * the unchanged URL otherwise.
     */
    @Override
    public String encodeURL(String url) {
        return url;
    }

    /**
     * Encodes the specified URL for use in the
     * <code>sendRedirect</code> method or, if encoding is not needed,
     * returns the URL unchanged.  The implementation of this method
     * includes the logic to determine whether the session ID
     * needs to be encoded in the URL.  For example, if the browser supports
     * cookies, or session tracking is turned off, URL encoding is
     * unnecessary.  Because the rules for making this determination can
     * differ from those used to decide whether to
     * encode a normal link, this method is separated from the
     * <code>encodeURL</code> method.
     *
     * <p>All URLs sent to the <code>HttpServletResponse.sendRedirect</code>
     * method should be run through this method.  Otherwise, URL
     * rewriting cannot be used with browsers which do not support
     * cookies.
     *
     * <p>If the URL is relative, it is always relative to the current
     * HttpServletRequest.
     *
     * @throws IllegalArgumentException if the url is not valid
     * @param    url    the url to be encoded.
     * @return the encoded URL if encoding is needed;
     * the unchanged URL otherwise.
     * @see #sendRedirect
     * @see #encodeUrl
     */
    @Override
    public String encodeRedirectURL(String url) {
        return url;
    }

    /**
     * @throws IllegalArgumentException if the url is not valid
     * @deprecated As of version 2.1, use encodeURL(String url) instead
     * @param    url    the url to be encoded.
     * @return the encoded URL if encoding is needed;
     * the unchanged URL otherwise.
     */
    @Override
    public String encodeUrl(String url) {
        return url;
    }

    /**
     * @throws IllegalArgumentException if the url is not valid
     * @deprecated As of version 2.1, use
     * encodeRedirectURL(String url) instead
     * @param    url    the url to be encoded.
     * @return the encoded URL if encoding is needed;
     * the unchanged URL otherwise.
     */
    @Override
    public String encodeRedirectUrl(String url) {
        return url;
    }

    /**
     * Sends an error response to the client using the specified
     * status and clears the buffer.  The server defaults to creating the
     * response to look like an HTML-formatted server error page
     * containing the specified message, setting the content type
     * to "text/html". The server will preserve cookies and may clear or
     * update any headers needed to serve the error page as a valid response.
     * <p>
     * If an error-page declaration has been made for the web application
     * corresponding to the status code passed in, it will be served back in
     * preference to the suggested msg parameter and the msg parameter will
     * be ignored.
     *
     * <p>If the response has already been committed, this method throws
     * an IllegalStateException.
     * After using this method, the response should be considered
     * to be committed and should not be written to.
     *
     * @param    sc    the error status code
     * @param    msg    the descriptive message
     * @exception IOException    If an input or output exception occurs
     * @exception IllegalStateException    If the response was committed
     */
    @Override
    public void sendError(int sc, String msg) throws IOException {

    }

    /**
     * Sends an error response to the client using the specified status
     * code and clears the buffer.
     * <p>
     * The server will preserve cookies and may clear or
     * update any headers needed to serve the error page as a valid response.
     * <p>
     * If an error-page declaration has been made for the web application
     * corresponding to the status code passed in, it will be served back
     * the error page
     *
     * <p>If the response has already been committed, this method throws
     * an IllegalStateException.
     * After using this method, the response should be considered
     * to be committed and should not be written to.
     *
     * @param    sc    the error status code
     * @exception IOException    If an input or output exception occurs
     * @exception IllegalStateException    If the response was committed
     * before this method call
     */
    @Override
    public void sendError(int sc) throws IOException {

    }

    /**
     * Sends a temporary redirect response to the client using the
     * specified redirect location URL and clears the buffer. The buffer will
     * be replaced with the data set by this method. Calling this method sets the
     * status code to {@link #SC_FOUND} 302 (Found).
     * This method can accept relative URLs;the servlet container must convert
     * the relative URL to an absolute URL
     * before sending the response to the client. If the location is relative
     * without a leading '/' the container interprets it as relative to
     * the current request URI. If the location is relative with a leading
     * '/' the container interprets it as relative to the servlet container root.
     * If the location is relative with two leading '/' the container interprets
     * it as a network-path reference (see
     * <a href="http://www.ietf.org/rfc/rfc3986.txt">
     * RFC 3986: Uniform Resource Identifier (URI): Generic Syntax</a>, section 4.2
     * &quot;Relative Reference&quot;).
     *
     * <p>If the response has already been committed, this method throws
     * an IllegalStateException.
     * After using this method, the response should be considered
     * to be committed and should not be written to.
     *
     * @param        location    the redirect location URL
     * @exception IOException    If an input or output exception occurs
     * @exception IllegalStateException    If the response was committed or
     * if a partial URL is given and cannot be converted into a valid URL
     */
    @Override
    public void sendRedirect(String location) throws IOException {

    }

    /**
     * Sets a response header with the given name and
     * date-value.  The date is specified in terms of
     * milliseconds since the epoch.  If the header had already
     * been set, the new value overwrites the previous one.  The
     * <code>containsHeader</code> method can be used to test for the
     * presence of a header before setting its value.
     *
     * @param    name    the name of the header to set
     * @param    date    the assigned date value
     * @see #containsHeader
     * @see #addDateHeader
     */
    @Override
    public void setDateHeader(String name, long date) {
        setHeader(name, FastHttpDateFormat.formatDate(date));
    }

    /**
     * Adds a response header with the given name and
     * date-value.  The date is specified in terms of
     * milliseconds since the epoch.  This method allows response headers
     * to have multiple values.
     *
     * @param    name    the name of the header to set
     * @param    date    the additional date value
     * @see #setDateHeader
     */
    @Override
    public void addDateHeader(String name, long date) {
        addHeader(name, FastHttpDateFormat.formatDate(date));
    }

    /**
     *
     * Sets a response header with the given name and value.
     * If the header had already been set, the new value overwrites the
     * previous one.  The <code>containsHeader</code> method can be
     * used to test for the presence of a header before setting its
     * value.
     *
     * @param	name	the name of the header
     * @param	value	the header value  If it contains octet string,
     *		it should be encoded according to RFC 2047
     *		(http://www.ietf.org/rfc/rfc2047.txt)
     *
     * @see #containsHeader
     * @see #addHeader
     */
    @Override
    public void setHeader(String name, String value) {
        headers.put(name, Collections.singletonList(value));
    }

    /**
     * Adds a response header with the given name and value.
     * This method allows response headers to have multiple values.
     *
     * @param    name    the name of the header
     * @param    value    the additional header value   If it contains
     * octet string, it should be encoded
     * according to RFC 2047
     * (http://www.ietf.org/rfc/rfc2047.txt)
     * @see #setHeader
     */
    @Override
    public void addHeader(String name, String value) {
        List<String> list = headers.get(name);
        if (list != null) {
            list.add(value);
        } else {
            setHeader(name, value);
        }
    }

    /**
     * Sets a response header with the given name and
     * integer value.  If the header had already been set, the new value
     * overwrites the previous one.  The <code>containsHeader</code>
     * method can be used to test for the presence of a header before
     * setting its value.
     *
     * @param    name    the name of the header
     * @param    value    the assigned integer value
     * @see #containsHeader
     * @see #addIntHeader
     */
    @Override
    public void setIntHeader(String name, int value) {
        setHeader(name, String.valueOf(value));
    }

    /**
     * Adds a response header with the given name and
     * integer value.  This method allows response headers to have multiple
     * values.
     *
     * @param    name    the name of the header
     * @param    value    the assigned integer value
     * @see #setIntHeader
     */
    @Override
    public void addIntHeader(String name, int value) {
        addHeader(name, String.valueOf(value));
    }

    /**
     * Sets the status code for this response.
     *
     * <p>This method is used to set the return status code when there is
     * no error (for example, for the SC_OK or SC_MOVED_TEMPORARILY status
     * codes).
     *
     * <p>If this method is used to set an error code, then the container's
     * error page mechanism will not be triggered. If there is an error and
     * the caller wishes to invoke an error page defined in the web
     * application, then {@link #sendError} must be used instead.
     *
     * <p>This method preserves any cookies and other response headers.
     *
     * <p>Valid status codes are those in the 2XX, 3XX, 4XX, and 5XX ranges.
     * Other status codes are treated as container specific.
     *
     * @param    sc    the status code
     * @see #sendError
     */
    @Override
    public void setStatus(int sc) {
        this.code = sc;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @deprecated As of version 2.1, due to ambiguous meaning of the
     * message parameter. To set a status code
     * use <code>setStatus(int)</code>, to send an error with a description
     * use <code>sendError(int, String)</code>.
     *
     * Sets the status code and message for this response.
     *
     * @param	sc	the status code
     * @param	sm	the status message
     */
    @Override
    public void setStatus(int sc, String sm) {
        this.code = sc;
        this.status = sm;
    }

    /**
     * Gets the current status code of this response.
     *
     * @return the current status code of this response
     * @since Servlet 3.0
     */
    @Override
    public int getStatus() {
        return code;
    }

    /**
     * Gets the value of the response header with the given name.
     *
     * <p>If a response header with the given name exists and contains
     * multiple values, the value that was added first will be returned.
     *
     * <p>This method considers only response headers set or added via
     * {@link #setHeader}, {@link #addHeader}, {@link #setDateHeader},
     * {@link #addDateHeader}, {@link #setIntHeader}, or
     * {@link #addIntHeader}, respectively.
     *
     * @param name the name of the response header whose value to return
     * @return the value of the response header with the given name,
     * or <tt>null</tt> if no header with the given name has been set
     * on this response
     * @since Servlet 3.0
     */
    @Override
    public String getHeader(String name) {
        List<String> list = headers.get(name);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Gets the values of the response header with the given name.
     *
     * <p>This method considers only response headers set or added via
     * {@link #setHeader}, {@link #addHeader}, {@link #setDateHeader},
     * {@link #addDateHeader}, {@link #setIntHeader}, or
     * {@link #addIntHeader}, respectively.
     *
     * <p>Any changes to the returned <code>Collection</code> must not
     * affect this <code>HttpServletResponse</code>.
     *
     * @param name the name of the response header whose values to return
     * @return a (possibly empty) <code>Collection</code> of the values
     * of the response header with the given name
     * @since Servlet 3.0
     */
    @Override
    public Collection<String> getHeaders(String name) {
        List<String> list = headers.get(name);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    /**
     * Gets the names of the headers of this response.
     *
     * <p>This method considers only response headers set or added via
     * {@link #setHeader}, {@link #addHeader}, {@link #setDateHeader},
     * {@link #addDateHeader}, {@link #setIntHeader}, or
     * {@link #addIntHeader}, respectively.
     *
     * <p>Any changes to the returned <code>Collection</code> must not
     * affect this <code>HttpServletResponse</code>.
     *
     * @return a (possibly empty) <code>Collection</code> of the names
     * of the headers of this response
     * @since Servlet 3.0
     */
    @Override
    public Collection<String> getHeaderNames() {
        return headers.keySet();
    }

    /**
     * Sets the preferred buffer size for the body of the response.
     * The servlet container will use a buffer at least as large as
     * the size requested.  The actual buffer size used can be found
     * using <code>getBufferSize</code>.
     *
     * <p>A larger buffer allows more content to be written before anything is
     * actually sent, thus providing the servlet with more time to set
     * appropriate status codes and headers.  A smaller buffer decreases
     * server memory load and allows the client to start receiving data more
     * quickly.
     *
     * <p>This method must be called before any response body content is
     * written; if content has been written or the response object has
     * been committed, this method throws an
     * <code>IllegalStateException</code>.
     *
     * @param size the preferred buffer size
     * @throws IllegalStateException if this method is called after
     *                               content has been written
     * @see #getBufferSize
     * @see #flushBuffer
     * @see #isCommitted
     * @see #reset
     */
    @Override
    public void setBufferSize(int size) {
        bufferSize = size;
    }

    /**
     * Returns the actual buffer size used for the response.  If no buffering
     * is used, this method returns 0.
     *
     * @return the actual buffer size used
     * @see #setBufferSize
     * @see #flushBuffer
     * @see #isCommitted
     * @see #reset
     */
    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * Forces any content in the buffer to be written to the client.  A call
     * to this method automatically commits the response, meaning the status
     * code and headers will be written.
     *
     * @see #setBufferSize
     * @see #getBufferSize
     * @see #isCommitted
     * @see #reset
     */
    @Override
    public void flushBuffer() throws IOException {

    }

    /**
     * Clears the content of the underlying buffer in the response without
     * clearing headers or status code. If the
     * response has been committed, this method throws an
     * <code>IllegalStateException</code>.
     *
     * @see #setBufferSize
     * @see #getBufferSize
     * @see #isCommitted
     * @see #reset
     * @since Servlet 2.3
     */
    @Override
    public void resetBuffer() {

    }

    /**
     * Returns a boolean indicating if the response has been
     * committed.  A committed response has already had its status
     * code and headers written.
     *
     * @return a boolean indicating if the response has been
     * committed
     * @see #setBufferSize
     * @see #getBufferSize
     * @see #flushBuffer
     * @see #reset
     */
    @Override
    public boolean isCommitted() {
        return committed;
    }

    /**
     * Clears any data that exists in the buffer as well as the status code,
     * headers.  The state of calling {@link #getWriter} or
     * {@link #getOutputStream} is also cleared.  It is legal, for instance,
     * to call {@link #getWriter}, {@link #reset} and then
     * {@link #getOutputStream}.  If {@link #getWriter} or
     * {@link #getOutputStream} have been called before this method,
     * then the corrresponding returned Writer or OutputStream will be
     * staled and the behavior of using the stale object is undefined.
     * If the response has been committed, this method throws an
     * <code>IllegalStateException</code>.
     *
     * @throws IllegalStateException if the response has already been
     *                               committed
     * @see #setBufferSize
     * @see #getBufferSize
     * @see #flushBuffer
     * @see #isCommitted
     */
    @Override
    public void reset() {

    }

    /**
     * Sets the locale of the response, if the response has not been
     * committed yet. It also sets the response's character encoding
     * appropriately for the locale, if the character encoding has not
     * been explicitly set using {@link #setContentType} or
     * {@link #setCharacterEncoding}, <code>getWriter</code> hasn't
     * been called yet, and the response hasn't been committed yet.
     * If the deployment descriptor contains a
     * <code>locale-encoding-mapping-list</code> element, and that
     * element provides a mapping for the given locale, that mapping
     * is used. Otherwise, the mapping from locale to character
     * encoding is container dependent.
     * <p>This method may be called repeatedly to change locale and
     * character encoding. The method has no effect if called after the
     * response has been committed. It does not set the response's
     * character encoding if it is called after {@link #setContentType}
     * has been called with a charset specification, after
     * {@link #setCharacterEncoding} has been called, after
     * <code>getWriter</code> has been called, or after the response
     * has been committed.
     * <p>Containers must communicate the locale and the character encoding
     * used for the servlet response's writer to the client if the protocol
     * provides a way for doing so. In the case of HTTP, the locale is
     * communicated via the <code>Content-Language</code> header,
     * the character encoding as part of the <code>Content-Type</code>
     * header for text media types. Note that the character encoding
     * cannot be communicated via HTTP headers if the servlet does not
     * specify a content type; however, it is still used to encode text
     * written via the servlet response's writer.
     *
     * @param loc the locale of the response
     * @see #getLocale
     * @see #setContentType
     * @see #setCharacterEncoding
     */
    @Override
    public void setLocale(Locale loc) {

    }

    /**
     * Returns the locale specified for this response
     * using the {@link #setLocale} method. Calls made to
     * <code>setLocale</code> after the response is committed
     * have no effect. If no locale has been specified,
     * the container's default locale is returned.
     *
     * @see #setLocale
     */
    @Override
    public Locale getLocale() {
        return null;
    }

    /**
     * Returns the name of the character encoding (MIME charset)
     * used for the body sent in this response.
     * The character encoding may have been specified explicitly
     * using the {@link #setCharacterEncoding} or
     * {@link #setContentType} methods, or implicitly using the
     * {@link #setLocale} method. Explicit specifications take
     * precedence over implicit specifications. Calls made
     * to these methods after <code>getWriter</code> has been
     * called or after the response has been committed have no
     * effect on the character encoding. If no character encoding
     * has been specified, <code>ISO-8859-1</code> is returned.
     * <p>See RFC 2047 (http://www.ietf.org/rfc/rfc2047.txt)
     * for more information about character encoding and MIME.
     *
     * @return a <code>String</code> specifying the name of
     * the character encoding, for example, <code>UTF-8</code>
     */
    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    /**
     * Returns the content type used for the MIME body
     * sent in this response. The content type proper must
     * have been specified using {@link #setContentType}
     * before the response is committed. If no content type
     * has been specified, this method returns null.
     * If a content type has been specified, and a
     * character encoding has been explicitly or implicitly
     * specified as described in {@link #getCharacterEncoding}
     * or {@link #getWriter} has been called,
     * the charset parameter is included in the string returned.
     * If no character encoding has been specified, the
     * charset parameter is omitted.
     *
     * @return a <code>String</code> specifying the content type,
     * for example, <code>text/html; charset=UTF-8</code>, or null
     * @since Servlet 2.4
     */
    @Override
    public String getContentType() {
        return getHeader("Content-Type");
    }

    /**
     * Returns a <code>PrintWriter</code> object that
     * can send character text to the client.
     * The <code>PrintWriter</code> uses the character
     * encoding returned by {@link #getCharacterEncoding}.
     * If the response's character encoding has not been
     * specified as described in <code>getCharacterEncoding</code>
     * (i.e., the method just returns the default value
     * <code>ISO-8859-1</code>), <code>getWriter</code>
     * updates it to <code>ISO-8859-1</code>.
     * <p>Calling flush() on the <code>PrintWriter</code>
     * commits the response.
     * <p>Either this method or {@link #getOutputStream} may be called
     * to write the body, not both, except when {@link #reset}
     * has been called.
     *
     * @return a <code>PrintWriter</code> object that
     * can return character data to the client
     * @throws UnsupportedEncodingException if the character encoding returned
     *                                      by <code>getCharacterEncoding</code> cannot be used
     * @throws IllegalStateException        if the <code>getOutputStream</code>
     *                                      method has already been called for this response object
     * @throws IOException                  if an input or output exception occurred
     * @see #getOutputStream
     * @see #setCharacterEncoding
     * @see #reset
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        if (printWriter == null) {
            printWriter = socketWrapper.getPrintWriter();
        }
        return printWriter;
    }

    /**
     * Returns a {@link ServletOutputStream} suitable for writing binary
     * data in the response. The servlet container does not encode the
     * binary data.
     *
     * <p> Calling flush() on the ServletOutputStream commits the response.
     * <p>
     * Either this method or {@link #getWriter} may
     * be called to write the body, not both, except when {@link #reset}
     * has been called.
     *
     * @return a {@link ServletOutputStream} for writing binary data
     * @throws IllegalStateException if the <code>getWriter</code> method
     *                               has been called on this response
     * @throws IOException           if an input or output exception occurred
     * @see #getWriter
     * @see #reset
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (outputStream == null) {
            outputStream = socketWrapper.getOutputStream();
        }
        return (ServletOutputStream) outputStream;
    }

    /**
     * Sets the character encoding (MIME charset) of the response
     * being sent to the client, for example, to UTF-8.
     * If the character encoding has already been set by
     * {@link #setContentType} or {@link #setLocale},
     * this method overrides it.
     * Calling {@link #setContentType} with the <code>String</code>
     * of <code>text/html</code> and calling
     * this method with the <code>String</code> of <code>UTF-8</code>
     * is equivalent with calling
     * <code>setContentType</code> with the <code>String</code> of
     * <code>text/html; charset=UTF-8</code>.
     * <p>This method can be called repeatedly to change the character
     * encoding.
     * This method has no effect if it is called after
     * <code>getWriter</code> has been
     * called or after the response has been committed.
     * <p>Containers must communicate the character encoding used for
     * the servlet response's writer to the client if the protocol
     * provides a way for doing so. In the case of HTTP, the character
     * encoding is communicated as part of the <code>Content-Type</code>
     * header for text media types. Note that the character encoding
     * cannot be communicated via HTTP headers if the servlet does not
     * specify a content type; however, it is still used to encode text
     * written via the servlet response's writer.
     *
     * @param charset a String specifying only the character set
     * defined by IANA Character Sets
     * (http://www.iana.org/assignments/character-sets)
     *
     * @see #setContentType
     * @see #setLocale
     *
     * @since Servlet 2.4
     */
    public void setCharacterEncoding(String charset) {
        characterEncoding = charset;
        if (getContentType() != null) {
            setContentType(getContentType() + ";charset=" + charset);
        }
    }

    /**
     * Sets the length of the content body in the response
     * In HTTP servlets, this method sets the HTTP Content-Length header.
     *
     * @param len an integer specifying the length of the
     *            content being returned to the client; sets the Content-Length header
     */
    @Override
    public void setContentLength(int len) {
        setHeader("Content-Length", String.valueOf(len));
    }

    /**
     * Sets the length of the content body in the response
     * In HTTP servlets, this method sets the HTTP Content-Length header.
     *
     * @param len a long specifying the length of the
     *            content being returned to the client; sets the Content-Length header
     * @since Servlet 3.1
     */
    @Override
    public void setContentLengthLong(long len) {
        setHeader("Content-Length", String.valueOf(len));
    }

    /**
     * Sets the content type of the response being sent to
     * the client, if the response has not been committed yet.
     * The given content type may include a character encoding
     * specification, for example, <code>text/html;charset=UTF-8</code>.
     * The response's character encoding is only set from the given
     * content type if this method is called before <code>getWriter</code>
     * is called.
     * <p>This method may be called repeatedly to change content type and
     * character encoding.
     * This method has no effect if called after the response
     * has been committed. It does not set the response's character
     * encoding if it is called after <code>getWriter</code>
     * has been called or after the response has been committed.
     * <p>Containers must communicate the content type and the character
     * encoding used for the servlet response's writer to the client if
     * the protocol provides a way for doing so. In the case of HTTP,
     * the <code>Content-Type</code> header is used.
     *
     * @param type a <code>String</code> specifying the MIME
     *             type of the content
     * @see #setLocale
     * @see #setCharacterEncoding
     * @see #getOutputStream
     * @see #getWriter
     */
    @Override
    public void setContentType(String type) {
        setHeader("Content-Type", type);
    }

    public static Response standardResponse() {
        Response resp = Response.builder()
                .protocol(HttpProtocol.HTTP_1_1)
                .code(HttpStatusCode.OK)
                .status(HttpStatusDescription.OK)
                .headers(new HashMap<>())
                .cookies(new ArrayList<>())
                .useWriter(false)
                .useStream(false)
                .build();
        resp.setHeader("Server", "FatCat/0.2");
        resp.setDateHeader("Data", System.currentTimeMillis());
        return resp;
    }

    /**
     * 为静态代理部分保留
     * @return 空体响应
     */
    public String getResponseHeadString() {
        return protocol + " " + code + " " + status + "\r\n" +
               this.getParamString() + "\r\n";
    }

    private String getParamString() {
        if (headers != null) {
            StringBuilder paramString = new StringBuilder();
            headers.forEach((key, value) -> value.forEach(each -> paramString.append(key).append(": ").append(each).append("\r\n")));
            return paramString.toString();
        }
        return "";
    }

    @Override
    public void recycle() {

    }

    public SocketWrapper getSocketWrapper() {
        return socketWrapper;
    }

    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getCommitted() {
        return committed;
    }

    public void setCommitted(Boolean committed) {
        this.committed = committed;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Boolean getUseWriter() {
        return useWriter;
    }

    public void setUseWriter(Boolean useWriter) {
        this.useWriter = useWriter;
    }

    public Boolean getUseStream() {
        return useStream;
    }

    public void setUseStream(Boolean useStream) {
        this.useStream = useStream;
    }
}
