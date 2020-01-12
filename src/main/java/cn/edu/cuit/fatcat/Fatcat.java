package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.container.servlet.TestServlet;
import cn.edu.cuit.linker.SocketWrapper;
import cn.edu.cuit.linker.io.standard.StandardServletWriter;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import java.io.PrintWriter;

public class Fatcat implements LifeCycle {
    private Request request;
    private Response response;
    private SocketWrapper socketWrapper;
    private TestServlet testServlet;

    public Fatcat(Request request, Response response, SocketWrapper socketWrapper) {
        this.request = request;
        this.response = response;
        this.socketWrapper = socketWrapper;
    }

    @Override
    public void init() throws Throwable {
        this.testServlet = new TestServlet(); // 实例化TestServlet
        this.response.setPrinter(this.socketWrapper.getPrintWriter()); // 初始化PrintWriter
    }

    @Override
    public void start() throws Throwable {
        PrintWriter out = this.response.getPrinter();
        out.println(this.response.getProtocol() + " " + response.getCode() + " " + response.getStatus()); // 响应头三条
        this.testServlet.service(this.request, this.response);
    }

    @Override
    public void stop() throws Throwable {
        StandardServletWriter.getWriter().write(this.request, this.response);
    }

    @Override
    public void destroy() throws Throwable {

    }
}
