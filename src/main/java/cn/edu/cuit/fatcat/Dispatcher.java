package cn.edu.cuit.fatcat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class Dispatcher implements RequestDispatcher {
    private String path;

    private Dispatcher(String path) {
        this.path = path;
    }

    public static RequestDispatcher create(String path) {
        return new Dispatcher(path);
    }

    /**
     * Forwards a request from a servlet to another resource (servlet, JSP file,
     * or HTML file) on the server. This method allows one servlet to do
     * preliminary processing of a request and another resource to generate the
     * response.
     *
     * <p>
     * For a <code>RequestDispatcher</code> obtained via
     * <code>getRequestDispatcher()</code>, the <code>ServletRequest</code>
     * object has its path elements and parameters adjusted to match the path of
     * the target resource.
     *
     * <p>
     * <code>forward</code> should be called before the response has been
     * committed to the client (before response body output has been flushed).
     * If the response already has been committed, this method throws an
     * <code>IllegalStateException</code>. Uncommitted output in the response
     * buffer is automatically cleared before the forward.
     *
     * <p>
     * The request and response parameters must be either the same objects as
     * were passed to the calling servlet's service method or be subclasses of
     * the {@link ServletRequestWrapper} or {@link ServletResponseWrapper}
     * classes that wrap them.
     *
     * @param request  a {@link ServletRequest} object that represents the request
     *                 the client makes of the servlet
     * @param response a {@link ServletResponse} object that represents the response
     *                 the servlet returns to the client
     * @throws ServletException      if the target resource throws this exception
     * @throws IOException           if the target resource throws this exception
     * @throws IllegalStateException if the response was already committed
     */
    @Override
    public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {

    }

    /**
     * Includes the content of a resource (servlet, JSP page, HTML file) in the
     * response. In essence, this method enables programmatic server-side
     * includes.
     *
     * <p>
     * The {@link ServletResponse} object has its path elements and parameters
     * remain unchanged from the caller's. The included servlet cannot change
     * the response status code or set headers; any attempt to make a change is
     * ignored.
     *
     * <p>
     * The request and response parameters must be either the same objects as
     * were passed to the calling servlet's service method or be subclasses of
     * the {@link ServletRequestWrapper} or {@link ServletResponseWrapper}
     * classes that wrap them.
     *
     * @param request  a {@link ServletRequest} object that contains the client's
     *                 request
     * @param response a {@link ServletResponse} object that contains the servlet's
     *                 response
     * @throws ServletException if the included resource throws this exception
     * @throws IOException      if the included resource throws this exception
     */
    @Override
    public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {

    }

}
