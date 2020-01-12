package cn.edu.cuit.fatcat.container.servlet;

import cn.edu.cuit.fatcat.http.HttpMethod;
import cn.edu.cuit.fatcat.http.HttpProtocol;
import cn.edu.cuit.fatcat.http.HttpStatusCode;
import cn.edu.cuit.fatcat.http.HttpStatusDescription;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.util.Date;

@Slf4j
public class TestServlet {

    public void service(Request request, Response response) {
        // 判断协议版本
        if (request.getProtocol().endsWith(HttpProtocol.VERSION)) {
            // 如果HTTP协议版本是1.1
            // 进行HTTP方法(动作)派分，处理
            switch (request.getMethod()) {
                case HttpMethod.METHOD_GET:
                    doGet(request, response);
                    break;
                case HttpMethod.METHOD_POST:
                    doPost(request, response);
                    break;
                case HttpMethod.METHOD_DELETE:
                    doDelete(request, response);
                    break;
                case HttpMethod.METHOD_PATCH:
                    doPatch(request, response);
                    break;
                case HttpMethod.METHOD_PUT:
                    doPut(request, response);
                    break;
                case HttpMethod.METHOD_HEAD:
                    doHead(request, response);
                    break;
                case HttpMethod.METHOD_OPTIONS:
                    doOptions(request, response);
                    break;
                case HttpMethod.METHOD_TRACE:
                    doTrace(request, response);
                    break;
                default:
                    // 如果请求方法不匹配以上所有方法，则报501错误
                    log.warn("未实现的方法: {}", request.getMethod());
                    response.setCode(HttpStatusCode.NOT_IMPLEMENTED);
                    response.setStatus(HttpStatusDescription.NOT_IMPLEMENTED);
                    break;
            }
        } else {
            // 如果协议不是HTTP/1.1，则报505错误
            response.setCode(HttpStatusCode.HTTP_VERSION_NOT_SUPPORTED);
            response.setStatus(HttpStatusDescription.HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    private void doGet(Request request, Response response) {
        PrintWriter out = response.getPrinter();
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Date", new Date().toString());
        response.setHeader("Server", "Fatcat");
        out.println(); // 怎么处理PrinteWriter???
        out.println("<h1>Hello, world !</h1>");
        out.println("这是一条来自" + this.getClass().getName() + ", " + this.toString() + ", 的消息.<br/>");
        out.println(request.getContext().replaceAll("\r\n", "<br/>\r\n"));
    }

    private void doPost(Request request, Response response) {

    }

    private void doDelete(Request request, Response response) {

    }

    private void doPatch(Request request, Response response) {

    }

    private void doPut(Request request, Response response) {

    }

    private void doHead(Request request, Response response) {

    }

    private void doOptions(Request request, Response response) {

    }

    private void doTrace(Request request, Response response) {

    }

}
