package cn.edu.cuit.fatcat.container.servlet;

import cn.edu.cuit.fatcat.http.HttpHeader;
import cn.edu.cuit.fatcat.io.FatCatOutPutStream;
import cn.edu.cuit.fatcat.io.FatCatWriter;
import cn.edu.cuit.fatcat.message.Request;
import cn.edu.cuit.fatcat.message.Response;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 用于初始化 + 调用Servlet
 */
@Slf4j
public enum ServletCaller {
    INSTANCE;

    public void callServlet(Request request, Response response, String servletName) throws ServletException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException {
        ServletContainer servletContainer = ServletCollector.getInstance().getServletContainer(servletName);
        log.info("URL: {}, 请求Servlet: {}", request.getDispatchedDirection(), ServletCollector.getInstance().getServletContainer(servletName));
        response.setHeader(HttpHeader.TRANSFER_ENCODING, "chunked");
        Servlet servlet = servletContainer.getInstance(); // 获取Servlet实例
        servlet.service(request, response); // 生命周期: 服务
        response.flushBuffer(); // 服务后刷新缓冲区
        sendEmptyChunk(response); // 输出空块向浏览器表示输出结束
    }

    private void sendEmptyChunk(Response response) throws IOException {
        if (response.isUseStream()) {
            FatCatOutPutStream out = (FatCatOutPutStream) response.getOutputStream();
            out.writeEmptyChunk();
        }
        if (response.isUseWriter()) {
            FatCatWriter out = (FatCatWriter) response.getWriter();
            out.writeEmptyChunk();
        }
    }
}
