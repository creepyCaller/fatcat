package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.io.Cache;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 转发服务
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
public enum Dispatcher implements RequestDispatcher {
    INSTANCE;

    private final Map<String, String> dispatchMap = new HashMap<>();

    public void setDispatch(String srcDir, String dstDir) {
        dispatchMap.put(srcDir, dstDir);
    }

    public String getDispatch(String srcDir) {
        return dispatchMap.get(srcDir);
    }

    /**
     * 请求路径转发
     *
     * @param direction 路径
     */
    public String dispatch(String direction) {
        if ("/".equals(direction)) {
            //这是对于请求根目录的情况
            if (Setting.WELCOME_LIST.size() > 0) {
                AtomicReference<String> ret = new AtomicReference<>();
                Setting.WELCOME_LIST.stream()
                        .filter(Dispatcher::nonEmpty)
                        .forEach(each -> {
                            if (checkWelcomeFile(each)) {
                                ret.set(each);
                            }
                        });
                return ret.get();
            } else {
                return Setting.DEFAULT_WELCOME;
            }
        } else {
            //除了根目录就看看有没有转发
            String dispatch = getDispatch(direction);
            if (dispatch != null) {
                return dispatch;
            }
        }
        return direction;
    }

    private static boolean nonEmpty(String s) {
        return s != null && s.length() > 0;
    }

    private static boolean checkWelcomeFile(String welcome) {
        if (Cache.INSTANCE.get(welcome) != null) {
            return true;
        } else {
            File file = new File(Setting.SERVER_ROOT + welcome);
            return file.exists();
        }
    }

    /**
     * Forwards a request from
     * a servlet to another resource (servlet, JSP file, or
     * HTML file) on the server. This method allows
     * one servlet to do preliminary processing of
     * a request and another resource to generate
     * the response.
     *
     * <p>For a <code>RequestDispatcher</code> obtained via
     * <code>getRequestDispatcher()</code>, the <code>ServletRequest</code>
     * object has its path elements and parameters adjusted to match
     * the path of the target resource.
     *
     * <p><code>forward</code> should be called before the response has been
     * committed to the client (before response body output has been flushed).
     * If the response already has been committed, this method throws
     * an <code>IllegalStateException</code>.
     * Uncommitted output in the response buffer is automatically cleared
     * before the forward.
     *
     * <p>The request and response parameters must be either the same
     * objects as were passed to the calling servlet's service method or be
     * subclasses of the {@link ServletRequestWrapper} or
     * {@link ServletResponseWrapper} classes
     * that wrap them.
     *
     * <p>This method sets the dispatcher type of the given request to
     * <code>DispatcherType.FORWARD</code>.
     *
     * @param request  a {@link ServletRequest} object that represents the
     *                 request the client makes of the servlet
     * @param response a {@link ServletResponse} object that represents
     *                 the response the servlet returns to the client
     * @throws ServletException      if the target resource throws this exception
     * @throws IOException           if the target resource throws this exception
     * @throws IllegalStateException if the response was already committed
     * @see ServletRequest#getDispatcherType
     */
    @Override
    public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {

    }

    /**
     * Includes the content of a resource (servlet, JSP page,
     * HTML file) in the response. In essence, this method enables
     * programmatic server-side includes.
     *
     * <p>The {@link ServletResponse} object has its path elements
     * and parameters remain unchanged from the caller's. The included
     * servlet cannot change the response status code or set headers;
     * any attempt to make a change is ignored.
     *
     * <p>The request and response parameters must be either the same
     * objects as were passed to the calling servlet's service method or be
     * subclasses of the {@link ServletRequestWrapper} or
     * {@link ServletResponseWrapper} classes that wrap them.
     *
     * <p>This method sets the dispatcher type of the given request to
     * <code>DispatcherType.INCLUDE</code>.
     *
     * @param request  a {@link ServletRequest} object that contains the
     *                 client's request
     * @param response a {@link ServletResponse} object that contains the
     *                 servlet's response
     * @throws ServletException if the included resource throws this
     *                          exception
     * @throws IOException      if the included resource throws this exception
     * @see ServletRequest#getDispatcherType
     */
    @Override
    public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {

    }
}
