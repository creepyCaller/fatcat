package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.container.servlet.Mapping;
import cn.edu.cuit.fatcat.container.servlet.ServletCollector;
import cn.edu.cuit.fatcat.container.servlet.ServletModel;
import cn.edu.cuit.fatcat.http.HttpContentType;
import cn.edu.cuit.fatcat.message.Request;
import cn.edu.cuit.fatcat.message.Response;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.Servlet;

/**
 * 一次访问对应从线程池获取一个实例（可回收），用于初始化 + 调用Servlet
 */
@Slf4j
public class ServletCaller implements Caller, RecycleAble {
    private Request request;
    private Response response;
    private Servlet servlet;

    public ServletCaller(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void init() throws Throwable {
        // TODO: lib下的jar文件咋办?
        response.setContentType(HttpContentType.TEXT_HTML);
        String servletName = Mapping.getServletName(request.getDirection());
        ServletModel servletModel = ServletCollector.getInstance().getServletModel(servletName);
        log.info("请求的Servlet: {}", ServletCollector.getInstance().getServletModel(servletName));
        servlet = servletModel.getInstance();
        log.info("servlet instance: {}", servlet);
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
