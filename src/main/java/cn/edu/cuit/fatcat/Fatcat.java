package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.container.servlet.Mapping;
import cn.edu.cuit.fatcat.container.servlet.ServletModel;
import cn.edu.cuit.fatcat.container.servlet.Servlets;
import cn.edu.cuit.fatcat.http.HttpContentType;
import cn.edu.cuit.linker.Server;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Servlet;

/**
 * 一次访问对应从线程池获取一个实例（可回收），用于初始化 + 调用Servlet
 * 双亲委派？
 */
@Slf4j
public class Fatcat implements LifeCycle, RecycleAble {
    private Request request;
    private Response response;
    private Servlet servlet;

    public Fatcat(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void init() throws Throwable {
        // TODO: lib下的jar文件咋办?
        response.setContentType(HttpContentType.TEXT_HTML);
        String servletName = Mapping.getServletName(request.getDirection());
        ServletModel servletModel = Server.servlets.getServletModel(servletName);
        log.info("请求的Servlet: {}", Server.servlets.getServletModel(servletName));
        servlet = servletModel.getInstance();
        servlet.init(servletModel);
    }

    @Override
    public void service() throws Throwable {
        // TODO: 搞清楚该怎么输出
        // TODO: 应该先输出到一个缓冲区？
        servlet.service(request, response);
    }

    @Override
    public void destroy() throws Throwable {
        servlet.destroy();
    }

    @Override
    public void recycle() {
        this.request = null;
        this.response = null;
        this.servlet = null;
    }
}
