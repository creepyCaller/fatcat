package cn.edu.cuit.fatcat.io.io;

import cn.edu.cuit.fatcat.RecycleAble;
import cn.edu.cuit.fatcat.adapter.ResponseAdapter;
import cn.edu.cuit.fatcat.container.servlet.ServletCaller;
import cn.edu.cuit.fatcat.container.servlet.ServletMapping;
import cn.edu.cuit.fatcat.handler.ExceptionHandler;
import cn.edu.cuit.fatcat.http.HttpHeader;
import cn.edu.cuit.fatcat.Setting;
import cn.edu.cuit.fatcat.http.HttpStatusCode;
import cn.edu.cuit.fatcat.http.HttpStatusDescription;
import cn.edu.cuit.fatcat.message.Request;
import cn.edu.cuit.fatcat.message.Response;
import cn.edu.cuit.fatcat.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 对输出的处理类
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
@Slf4j
public class StandardWriter implements AutoCloseable, RecycleAble {
    private OutputStream oStr;

    public StandardWriter(OutputStream oStr) {
        this.oStr = oStr;
    }

    private void write(byte[] responseHead, byte[] responseBody) throws IOException {
        oStr.write(responseHead);
        oStr.write(responseBody);
        oStr.flush();
    }

    public void write(Request request, Response response) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ServletException, IOException {
        try {
            byte[] responseBody = FileUtil.readBinStr(request, response);
            response.setHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(responseBody.length));
            byte[] responseHead = ResponseAdapter.INSTANCE.getResponseHead(response).getBytes(Setting.CHARSET);
            write(responseHead, responseBody);
        } catch (FileNotFoundException e) {
            // 404 Not Found
            exceptionHandler(request, response, 404);
        } catch (IOException | InterruptedException e) {
            // 500 Internal Server Error
            exceptionHandler(request, response, 500);
        }
    }

    private void exceptionHandler(Request request, Response response, Integer sc) throws ClassNotFoundException, IOException, InstantiationException, ServletException, IllegalAccessException {
        String direction = Setting.ERROR_PAGES.get(sc);
        if (ServletMapping.INSTANCE.getServletName(direction) != null) {
            request.setDirection(direction);
            ServletCaller.INSTANCE.callServlet(request, response);
        } else {
            byte[] responseBody = ExceptionHandler.generateErrorPage(request, response, HttpStatusCode.INTERNAL_SERVER_ERROR, HttpStatusDescription.INTERNAL_SERVER_ERROR);
            response.setHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(responseBody.length));
            byte[] responseHead = ResponseAdapter.INSTANCE.getResponseHead(response).getBytes(Setting.CHARSET);
            write(responseHead, responseBody);
        }
    }

    @Override
    public void close() throws IOException {
        oStr.close();
    }

    @Override
    public void recycle() {

    }
}
