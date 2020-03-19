package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.container.servlet.Mapping;
import cn.edu.cuit.fatcat.container.servlet.Servlet;
import cn.edu.cuit.fatcat.container.servlet.ServletModel;
import cn.edu.cuit.fatcat.container.servlet.Servlets;
import cn.edu.cuit.fatcat.test.TestServlet;
import cn.edu.cuit.linker.Server;
import cn.edu.cuit.linker.SocketWrapper;
import cn.edu.cuit.linker.io.standard.StandardServletWriter;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;

/**
 * 用于调用Servlet
 * 双亲委派？
 */
@Slf4j
public class Fatcat implements LifeCycle {
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
        // TODO: 把TestServlet的class文件放到webapp下看看怎么加载
        // TODO: lib下的jar文件咋办?
        String servletName = Mapping.getServletName(request.getDirection());
        log.info("请求的servletName: {}", servletName);
        ServletModel servletModel = Servlets.getServletModel(servletName);
        log.info("请求的ServletModel: {}", Servlets.getServletModel(servletName));
        if (servletModel.getServletInstance() == null) {
            servlet = (Servlet) Server.servletInstanceManager.newInstanceByClazzName(servletModel.getServletClazz());
            servletModel.setServletInstance(servlet);
        } else {
            servlet = servletModel.getInstance();
        }
        response.setPrinter(socketWrapper.getPrintWriter()); // 初始化PrintWriter
    }

    @Override
    public void service() throws Throwable {
        // TODO: 搞清楚该怎么输出
        PrintWriter out = response.getPrinter();
        out.println(response.getProtocol() + " " + response.getCode() + " " + response.getStatus()); // 响应头三条
        servlet.service(request, response);
    }

    @Override
    public void destroy() throws Throwable {
        StandardServletWriter.getWriter().write(request, response);
    }
}
