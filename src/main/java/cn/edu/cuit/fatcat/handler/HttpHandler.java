package cn.edu.cuit.fatcat.handler;

import cn.edu.cuit.fatcat.Caller;
import cn.edu.cuit.fatcat.RunnableFunctionalModule;
import cn.edu.cuit.fatcat.ServletCaller;
import cn.edu.cuit.fatcat.Setting;
import cn.edu.cuit.fatcat.adapter.RequestAdapter;
import cn.edu.cuit.fatcat.container.servlet.Mapping;
import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.fatcat.io.SocketWrapper;
import cn.edu.cuit.fatcat.io.standard.StandardReader;
import cn.edu.cuit.fatcat.io.standard.StandardWriter;
import cn.edu.cuit.fatcat.message.Request;
import cn.edu.cuit.fatcat.message.Response;
import cn.edu.cuit.fatcat.util.FileUtil;
import cn.edu.cuit.fatcat.util.ResponseMessageUtil;
import cn.edu.cuit.fatcat.Dispatcher;
import lombok.extern.slf4j.Slf4j;

/**
 * 对处理一次HTTP请求的子线程
 *
 * @author fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Slf4j
public class HttpHandler implements RunnableFunctionalModule {
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
        try(StandardReader reader = new StandardReader(socketWrapper.getInputStream());
            StandardWriter writer = new StandardWriter(socketWrapper.getOutputStream())) {
            String requestText = reader.readText();
            request = RequestAdapter.getInstance().getRequest(requestText);
            response = Response.standardResponse();
            response.setSocketWrapper(socketWrapper);
            init(); // 对请求和响应进行初始化
            String requestServletName = Mapping.getServletName(request.getDirection());
            if (requestServletName != null) {
                // Servlet容器:
                // TODO: 每个请求对应的ServletCaller实例,由池分配
                // TODO: 使用ResponseAdapter转换为响应报文再write
                Caller servletCaller = new ServletCaller(request, response);
                servletCaller.start();
            } else {
                // 反向代理:
                String suffix = FileUtil.getFileSuffix(request.getDirection());
                String contentType = ResponseMessageUtil.getContentType(suffix); // 判断文件类型
                response.setContentType(contentType);
                if (contentType.startsWith("text/") || contentType.startsWith("application/")) {
                    // 判断是二进制流还是文本文件
                    // 如果是文本文件,就设置响应头的编码类型
                    // TODO:这个判断需要改进（意思是多几个clause）
                    response.setCharacterEncoding(Setting.CHARSET_STRING);
                } else {
                    // 如果是字节流就设置流长度
                    response.setHeader(HttpHeader.ACCEPT_RANGES, "bytes");
                }
                writer.write(request, response);
            }
        } catch (Throwable e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }

    private void init() {
        // 首先调用转发器可能的请求url转发
        Dispatcher.getInstance().dispatch(request); // 处理转发
    }

    /**
     * 准备干活
     */
    @Override
    public void prepare() {

    }

    /**
     * 干活
     */
    @Override
    public void work() {

    }

    /**
     * 收工
     */
    @Override
    public void done() {

    }
}
