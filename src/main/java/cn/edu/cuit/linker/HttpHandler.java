package cn.edu.cuit.linker;

import cn.edu.cuit.fatcat.Fatcat;
import cn.edu.cuit.fatcat.LifeCycle;
import cn.edu.cuit.fatcat.container.servlet.TestServlet;
import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.linker.SocketWrapper;
import cn.edu.cuit.linker.io.Cache;
import cn.edu.cuit.linker.io.Reader;
import cn.edu.cuit.linker.io.standard.StandardServletWriter;
import cn.edu.cuit.linker.io.standard.StandardCache;
import cn.edu.cuit.linker.io.standard.StandardReader;
import cn.edu.cuit.linker.io.standard.StandardWriter;
import cn.edu.cuit.linker.io.Writer;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import cn.edu.cuit.linker.service.Dispatch;
import cn.edu.cuit.linker.adapter.RequestAdapter;
import cn.edu.cuit.linker.adapter.ResponseAdapter;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import cn.edu.cuit.linker.util.FileUtil;
import cn.edu.cuit.linker.util.ResponseMessageUtil;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.util.Collections;

/**
 * 对处理一次HTTP请求的子线程
 *
 * @author fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Slf4j
public class HttpHandler implements Runnable {
    private Request request;
    private Response response;
    private SocketWrapper socketWrapper;
    private boolean isRequestServlet = false;

    HttpHandler(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    /**
     * 这个线程要和分离
     * @see Thread#run()
     */
    @Override
    public void run() {
        try(Reader standardReader = new StandardReader(socketWrapper.getInputStream());
            Writer standardWriter = new StandardWriter(socketWrapper.getOutputStream())) {
            RequestAdapter requestAdapter = new RequestAdapter();
            ResponseAdapter responseAdapter = new ResponseAdapter();
            String requestText = standardReader.readText();
            this.request = requestAdapter.getRequest(requestText); // 构造请求报文对象
            this.response = Response.standardResponseMessageHead(); // 构造标准响应头
            this.init(); // 对请求和响应进行初始化
            // TODO:在这里进行派分,是请求Servlet还是服务器上的资源
            if (this.isRequestServlet) {
                // 如果是请求Servlet(暂未实现)(Servlet容器模块):
                LifeCycle fatcat = new Fatcat(request, response, socketWrapper);
                fatcat.service();;
            } else {
                // 如果是请求资源(请求资源的话就统一是二进制流)(反向代理模块):
                standardWriter.write(this.request, this.response);
            }
        } catch (Throwable e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }

    private void init() {
        // 首先调用转发器可能的请求url转发
        // 再判断转发后的文件类型
        // TODO:区分为反向代理部分(先把这个做好),Servlet容器部分(还没做)
        Dispatch.dispatch(this.request);
        String direction = this.request.getDirection();
        switch (direction) {
            case "$SERVLET$":
                this.isRequestServlet = true;
                return;
            case "$TEST$":
                this.response.setContentType(HttpContentType.TEXT_HTML);
                this.response.setCharacterEncoding(FatcatSetting.CHARSET_STRING);
                return;
        }
        String suffix = FileUtil.getFileSuffix(direction);
        String contentType = ResponseMessageUtil.getContentType(suffix);
        if (contentType.startsWith("text") || contentType.startsWith("application")) {
            // 判断是二进制流还是文本文件
            // 如果是文本文件,就设置响应头的编码类型
            // TODO:这个判断需要改进
            this.response.setCharacterEncoding(FatcatSetting.CHARSET_STRING);
        } else {
            this.response.setHeader(HttpHeader.ACCEPT_RANGES, "bytes");
        }
        this.response.setContentType(contentType);
    }
}
