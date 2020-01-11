package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.container.servlet.TestServlet;
import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.linker.SocketHandler;
import cn.edu.cuit.linker.handler.ExceptionHandler;
import cn.edu.cuit.linker.io.Reader;
import cn.edu.cuit.linker.io.standard.ServletWriter;
import cn.edu.cuit.linker.io.standard.StandardReader;
import cn.edu.cuit.linker.io.standard.StandardWriter;
import cn.edu.cuit.linker.io.Writer;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import cn.edu.cuit.linker.service.DispatchService;
import cn.edu.cuit.linker.adapter.RequestAdapter;
import cn.edu.cuit.linker.adapter.ResponseAdapter;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import cn.edu.cuit.linker.util.FileUtil;
import cn.edu.cuit.linker.util.ResponseMessageUtil;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

/**
 * 对处理一次HTTP请求的子线程
 *
 * @author fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Slf4j
public class Fatcat implements Runnable {
    private Request request;
    private Response response;
    private SocketHandler socketHandler;
    private ExceptionHandler exceptionHandler;
    private DispatchService dispatcherService;
    private ResponseAdapter responseAdapter;
    private RequestAdapter requestAdapter;

    boolean isRequestServlet = false;

    public Fatcat(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
        this.dispatcherService = new DispatchService();
    }

    /**
     * 这个线程要和分离
     * @see Thread#run()
     */
    @Override
    public void run() {
        try(Reader standardReader = new StandardReader(socketHandler.getInputStream());
            Writer standardWriter = new StandardWriter(socketHandler.getOutputStream());
            Writer servletWriter = new ServletWriter()) {
            this.requestAdapter = new RequestAdapter();
            this.responseAdapter = new ResponseAdapter();
            String requestText = standardReader.readText();
            this.request = requestAdapter.getRequest(requestText); // 构造请求报文对象
            this.response = Response.standardResponseMessageHead(); // 构造标准响应头
            this.init(request, response); // 对请求和响应进行初始化
            // TODO:在这里进行派分,是请求Servlet还是服务器上的资源
            if (this.isRequestServlet) {
                // 如果是请求Servlet(暂未实现)(Servlet容器模块):
                TestServlet testServlet = new TestServlet(); // 实例化TestServlet
                response.setPrinter(socketHandler.getPrintWriter()); // 初始化PrintWriter
                PrintWriter out = response.getPrinter();
                out.println(response.getProtocol() + " " + response.getCode() + " " + response.getStatus()); // 响应头三条
                testServlet.service(request, response);
                servletWriter.write(request, response);
            } else {
                // 如果是请求资源(请求资源的话就统一是二进制流)(反向代理模块):
                standardWriter.write(request, response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(Request request, Response response) {
        // 首先调用转发器可能的请求url转发
        // 再判断转发后的文件类型
        // TODO:区分为反向代理部分(先把这个做好),Servlet容器部分(还没做)
        dispatcherService.dispatch(request);
        String direction = request.getDirection();
        switch (direction) {
            case "$SERVLET$":
                this.isRequestServlet = true;
                return;
            case "$TEST$":
                response.setContentType(HttpContentType.TEXT_HTML);
                response.setCharacterEncoding(FatcatSetting.CHARSET_STRING);
                return;
        }
        String suffix = FileUtil.getFileSuffix(direction);
        String contentType = ResponseMessageUtil.getContentType(suffix);
        if (contentType.startsWith("text") || contentType.startsWith("application")) {
            // 判断是二进制流还是文本文件
            // 如果是文本文件,就设置响应头的编码类型
            // TODO:这个判断需要改进
            response.setCharacterEncoding(FatcatSetting.CHARSET_STRING);
        } else {
            response.getHeader().put(HttpHeader.ACCEPT_RANGES, Collections.singletonList("bytes"));
        }
        response.setContentType(contentType);
    }
}
