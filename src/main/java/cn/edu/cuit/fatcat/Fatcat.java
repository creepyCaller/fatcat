package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.container.servlet.Mapping;
import cn.edu.cuit.fatcat.container.servlet.Servlet;
import cn.edu.cuit.fatcat.container.servlet.ServletModel;
import cn.edu.cuit.fatcat.container.servlet.Servlets;
import cn.edu.cuit.linker.SocketWrapper;
import cn.edu.cuit.linker.io.standard.StandardServletWriter;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import lombok.extern.slf4j.Slf4j;
import java.io.PrintWriter;

/**
 * 一次访问对应从线程池获取一个实例（可回收），用于初始化 + 调用Servlet
 * 双亲委派？
 */
@Slf4j
public class Fatcat implements LifeCycle, RecycleAble {
    private Request request;
    private Response response;
    private SocketWrapper socketWrapper;
    private Servlet servlet;

    public Fatcat(Request request, Response response, SocketWrapper socketWrapper) {
        this.request = request;
        this.response = response;
        this.socketWrapper = socketWrapper;
    }

    @Override
    public void init() throws Throwable {
        // TODO: lib下的jar文件咋办?
        String servletName = Mapping.getServletName(request.getDirection());
        ServletModel servletModel = Servlets.getServletModel(servletName);
        log.info("请求的Servlet: {}", Servlets.getServletModel(servletName));
        servlet = servletModel.getInstance();
        response.setPrinter(socketWrapper.getPrintWriter()); // 初始化PrintWriter
    }

    @Override
    public void service() throws Throwable {
        // TODO: 搞清楚该怎么输出
        // TODO: 应该先输出到一个缓冲区？
        PrintWriter out = response.getPrinter();
        out.println(response.getProtocol() + " " + response.getCode() + " " + response.getStatus()); // 响应头三条
        servlet.service(request, response);
    }

    @Override
    public void destroy() throws Throwable {
        StandardServletWriter.getWriter().write(request, response);
    }

    @Override
    public void recycle() {
        this.request = null;
        this.response = null;
        this.servlet = null;
        this.socketWrapper = null;
    }
}
