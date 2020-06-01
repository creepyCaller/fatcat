package cn.edu.cuit.fatcat.io.io;

import cn.edu.cuit.fatcat.RecycleAble;
import cn.edu.cuit.fatcat.adapter.ResponseAdapter;
import cn.edu.cuit.fatcat.container.servlet.ServletCaller;
import cn.edu.cuit.fatcat.container.servlet.ServletCollector;
import cn.edu.cuit.fatcat.container.servlet.ServletMapping;
import cn.edu.cuit.fatcat.handler.ExceptionHandler;
import cn.edu.cuit.fatcat.http.HttpHeader;
import cn.edu.cuit.fatcat.Setting;
import cn.edu.cuit.fatcat.http.HttpStatusCode;
import cn.edu.cuit.fatcat.http.HttpStatusDescription;
import cn.edu.cuit.fatcat.io.Cache;
import cn.edu.cuit.fatcat.message.Request;
import cn.edu.cuit.fatcat.message.Response;
import cn.edu.cuit.fatcat.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

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
    private ByteBuffer bb;
    private byte[] byteArrayBuf;

    private StandardWriter(OutputStream oStr) {
        this.oStr = oStr;
    }

    public static StandardWriter getWriter(OutputStream oStr) {
        return new StandardWriter(oStr);
    }

    private void write(byte[] responseHead, byte[] responseBody) throws IOException {
        oStr.write(responseHead);
        oStr.write(responseBody);
        oStr.flush();
    }

    private void write(byte[] responseHead, Cache.Entry entry) throws IOException {
        oStr.write(responseHead);
        FileChannel fileChannel = entry.getChannel();
        if (bb == null) {
            bb = ByteBuffer.allocateDirect(Cache.Entry.capacity); // 申请1M的bb
        }
        byteArrayBuf = new byte[Cache.Entry.capacity]; // 申请1M大小的字节数组用作流输出的cache, 有点脱裤子放屁的意思, TODO: 把输出流换成Channel
        while (fileChannel.read(bb) != -1) {
            bb.get(byteArrayBuf);
            oStr.write(byteArrayBuf, 0, bb.limit() - bb.position()); // 获取ByteBuffer种已读取字节长度
            bb.clear();
        }
        oStr.flush();
    }

    public void write(Request request, Response response) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ServletException, IOException {
        try {
            if (request.getDirection().startsWith("/WEB-INF")) {
                // 拦截要访问WEB-INF文件夹的请求, 意思是未经转发的请求以/WEB-INF开头的话就说找不到, 转发过的就不管
                throw new FileNotFoundException();
            }
            Cache.Entry entry = Cache.INSTANCE.get(request.getDispatchedDirection()); // 先尝试从cache获取
            if (entry == null) {
                // 如果cache里没有记录, 就尝试新增记录
                FileUtil.putToCache(request.getDispatchedDirection());
                // 如果新增记录途中没有抛出异常, 说明把文件放入cache成功了
                entry = Cache.INSTANCE.get(request.getDispatchedDirection());
            }
            response.setHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(entry.getSize())); // 从cache读出文件大小作为响应体长度
            String mimeType = FileUtil.getMimeType(request.getDispatchedDirection()); // 通过后缀名判断文件类型
            response.setContentType(mimeType); // 设置响应报文的Content-Type
            byte[] responseHead = ResponseAdapter.INSTANCE.getResponseHead(response).getBytes(Setting.CHARSET);
            if (entry.getSize() < Cache.Entry.capacity) {
                // 如果文件大小小于缓冲块大小就直接读入context
                byte[] responseBody = entry.getContext();
                write(responseHead, responseBody);
            } else {
                // 如果文件太大就需要流输出
                write(responseHead, entry);
            }
        } catch (FileNotFoundException e) {
            // 404 Not Found
            exceptionHandler(request, response, 404);
        } catch (IOException e) {
            // 500 Internal Server Error
            exceptionHandler(request, response, 500);
        }
    }

    private void exceptionHandler(Request request, Response response, Integer sc) throws ClassNotFoundException, IOException, InstantiationException, ServletException, IllegalAccessException {
        String direction = Setting.ERROR_PAGES.get(sc); // 获取错误页地址
        request.setDispatchedDirection(direction); // 设置请求地址为错误页地址
        String servletName = ServletMapping.INSTANCE.getServletName(direction); // 查看该地址是否映射向servlet
        if (servletName != null) {
            // 如果设置的错误页指向Servlet
            ServletCaller.INSTANCE.callServlet(request, response, servletName);
        } else {
            // 如果设置的错误页指向静态资源
            byte[] responseBody;
            try {
                responseBody = FileUtil.readBinStr(request, response);
            } catch (FileNotFoundException e) {
                // 404 Not Found
                responseBody = ExceptionHandler.generateErrorPage(request, response, HttpStatusCode.NOT_FOUND, HttpStatusDescription.NOT_FOUND);
            } catch (IOException e) {
                // 500 Internal Server Error
                responseBody = ExceptionHandler.generateErrorPage(request, response, HttpStatusCode.INTERNAL_SERVER_ERROR, HttpStatusDescription.INTERNAL_SERVER_ERROR);
            }
            byte[] responseHead = ResponseAdapter.INSTANCE.getResponseHead(response).getBytes();
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
