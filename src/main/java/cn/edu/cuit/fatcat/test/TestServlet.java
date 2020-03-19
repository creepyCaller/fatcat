package cn.edu.cuit.fatcat.test;

import cn.edu.cuit.fatcat.container.servlet.Servlet;
import cn.edu.cuit.fatcat.http.HttpMethod;
import cn.edu.cuit.fatcat.http.HttpProtocol;
import cn.edu.cuit.fatcat.http.HttpStatusCode;
import cn.edu.cuit.fatcat.http.HttpStatusDescription;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import java.io.PrintWriter;
import java.util.Date;

public class TestServlet implements Servlet {

    @Override
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
        response.setCharacterEncoding("utf-8");
        out.println(); // 怎么处理PrinteWriter???
        out.println("<h1>Hello, world !</h1><br/>");
        out.println(this.toString() + "<br/>");
        out.println("Date=" + new Date().toString() + "<br/>");
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
