package cn.edu.cuit.fatcat.servlet;

import java.io.IOException;

/**
 * 接收HTTP请求报文并给出响应
 *
 * @author fpc
 * @date  2019/10/23
 * @see javax.servlet.Servlet
 * @since Fatcat 0.0.1
 */
public interface Servlet {

    /**
     * 在调用service()前调用，进行Servlet初始化
     *
     */
    public void init() throws IOException;

    /**
     * 对request进行处理并构造response
     *
     * @param request HTTP请求报文
     * @param response HTTP响应报文
     */
    public void service(String request, String response);

    /**
     * 将实例交给GC前调用，关闭各种流
     *
     */
    public void destroy() throws IOException;

}
