package cn.edu.cuit.fatcat.io.io;

import cn.edu.cuit.fatcat.RecycleAble;
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
    private static byte[] eol = "".getBytes();

    public StandardWriter(OutputStream oStr) {
        this.oStr = oStr;
    }

    private void write(byte[] responseHead, byte[] responseBody) throws IOException {
        oStr.write(responseHead);
        oStr.write(responseBody);
        oStr.flush();
    }

    public void write(Request request, Response response) throws Throwable {
        try {
            byte[] responseBody = FileUtil.readBinStr(request, response);
            response.setHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(responseBody.length));
            byte[] responseHead = response.getResponseHeadString().getBytes(Setting.CHARSET);
            write(responseHead, responseBody);
        } catch (FileNotFoundException e) {
            // 404 Not Found
            log.info("找不到文件: {}", request.getDirection());
            exceptionHandler(request, response, 404);
        } catch (IOException e) {
            // 500 Internal Server Error
            exceptionHandler(request, response, 500);
        }
    }

    private void exceptionHandler(Request request, Response response, Integer sc) throws Throwable {
        String direction = Setting.ERROR_PAGES.get(sc);
        if (ServletMapping.getInstance().getServletName(direction) != null) {
            request.setDirection(direction);
            (new ServletCaller(request, response)).start();
        } else {
            byte[] responseBody = ExceptionHandler.generateErrorPage(request, response, HttpStatusCode.INTERNAL_SERVER_ERROR, HttpStatusDescription.INTERNAL_SERVER_ERROR);
            response.setHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(responseBody.length));
            byte[] responseHead = response.getResponseHeadString().getBytes(Setting.CHARSET);
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
