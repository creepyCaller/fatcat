package cn.edu.cuit.fatcat.container.servlet;

import cn.edu.cuit.fatcat.http.HttpHeader;
import cn.edu.cuit.fatcat.io.FatCatOutPutStream;
import cn.edu.cuit.fatcat.message.Request;
import cn.edu.cuit.fatcat.message.Response;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用于初始化 + 调用Servlet
 */
@Slf4j
public enum ServletCaller {
    INSTANCE;

    public void callServlet(Request request, Response response) throws ServletException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException {
        String servletName = ServletMapping.INSTANCE.getServletName(request.getDirection());
        ServletContainer servletContainer = ServletCollector.getInstance().getServletModel(servletName);
        log.info("URL: {}, 请求Servlet: {}", request.getDirection(), ServletCollector.getInstance().getServletModel(servletName));
        response.setHeader(HttpHeader.TRANSFER_ENCODING, "chunked");
        Servlet servlet = servletContainer.getInstance();
        servlet.init(servletContainer);
        servlet.service(request, response);
        response.flushBuffer();
        sendEmptyChunk(response);
        servlet.destroy();
    }

    private void sendEmptyChunk(Response response) throws IOException {
        if (response.isUseStream()) {
            FatCatOutPutStream out = (FatCatOutPutStream) response.getOutputStream();
            out.writeEmptyChunk();
        }
        if (response.isUseWriter()) {
            PrintWriter out = response.getWriter();
        }
    }
}
