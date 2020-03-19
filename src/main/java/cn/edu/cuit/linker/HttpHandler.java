package cn.edu.cuit.linker;

import cn.edu.cuit.fatcat.Fatcat;
import cn.edu.cuit.fatcat.LifeCycle;
import cn.edu.cuit.fatcat.container.servlet.Mapping;
import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.linker.io.Reader;
import cn.edu.cuit.linker.io.standard.StandardReader;
import cn.edu.cuit.linker.io.standard.StandardWriter;
import cn.edu.cuit.linker.io.Writer;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import cn.edu.cuit.linker.service.Dispatcher;
import cn.edu.cuit.linker.adapter.RequestAdapter;
import cn.edu.cuit.linker.adapter.ResponseAdapter;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import cn.edu.cuit.linker.util.FileUtil;
import cn.edu.cuit.linker.util.ResponseMessageUtil;
import lombok.extern.slf4j.Slf4j;

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

    HttpHandler(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    /**
     * 这个线程要和分离
     * @see Thread#run()
     */
    @Override
    public void run() {
        try(Reader reader = new StandardReader(socketWrapper.getInputStream());
            Writer writer = new StandardWriter(socketWrapper.getOutputStream())) {
            RequestAdapter requestAdapter = new RequestAdapter();
            ResponseAdapter responseAdapter = new ResponseAdapter();
            String requestText = reader.readText();
            request = requestAdapter.getRequest(requestText); // 构造请求报文对象
            response = Response.standardResponseMessageHead(); // 构造标准响应头
            init(); // 对请求和响应进行初始化
            String requestServletName = Mapping.getServletName(request.getDirection());
            if (requestServletName != null) {
                // 如果是请求Servlet(暂未实现)(Servlet容器模块):
                // TODO: 在此处测试从别的地方调用class文件
                LifeCycle fatcat = new Fatcat(request, response, socketWrapper);
                fatcat.start();
            } else {
                // 如果是请求资源(请求资源的话就统一是二进制流)(反向代理模块):
                String suffix = FileUtil.getFileSuffix(request.getDirection());
                String contentType = ResponseMessageUtil.getContentType(suffix); // 判断文件类型
                if (contentType.startsWith("text/") || contentType.startsWith("application/")) {
                    // 判断是二进制流还是文本文件
                    // 如果是文本文件,就设置响应头的编码类型
                    // TODO:这个判断需要改进
                    response.setCharacterEncoding(FatcatSetting.CHARSET_STRING);
                } else {
                    // 如果是字节流就设置流长度
                    response.setHeader(HttpHeader.ACCEPT_RANGES, "bytes");
                }
                response.setContentType(contentType);
                writer.write(request, response);
            }
        } catch (Throwable e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }

    private void init() {
        // 首先调用转发器可能的请求url转发
        Dispatcher.dispatch(request); // 处理转发
    }
}
