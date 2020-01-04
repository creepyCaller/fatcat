package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.linker.io.Reader;
import cn.edu.cuit.linker.io.standard.StandardReader;
import cn.edu.cuit.linker.io.standard.StandardWriter;
import cn.edu.cuit.linker.io.Writer;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.ResponseHead;
import cn.edu.cuit.fatcat.service.DispatcherService;
import cn.edu.cuit.linker.service.RequestMessageService;
import cn.edu.cuit.linker.service.ResponseMessageService;
import cn.edu.cuit.fatcat.setting.WebApplicationServerSetting;
import cn.edu.cuit.linker.util.ArrayUtil;
import cn.edu.cuit.linker.util.FileUtil;
import cn.edu.cuit.linker.util.ResponseMessageUtil;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.Socket;

/**
 * 对处理一次HTTP请求的子线程
 *
 * @author fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Slf4j
public class GenesisServlet implements Runnable {

    private Socket socket;

    private DispatcherService dispatcherService;

    public GenesisServlet(Socket socket) {
        this.socket = socket;
        this.dispatcherService = new DispatcherService();
    }

    /**
     * 这个线程要和分离
     * @see Thread#run()
     */
    @Override
    public void run() {
        Request request;
        ResponseHead responseHead;
        try(Reader standardReader = new StandardReader(socket.getInputStream());
            Writer standardWriter = new StandardWriter(socket.getOutputStream())) {
            RequestMessageService requestMessageService = new RequestMessageService();
            ResponseMessageService responseMessageService = new ResponseMessageService(standardReader);
            request = requestMessageService.getRequestMessage(standardReader); // 构造请求报文对象
            responseHead = ResponseHead.standardResponseMessageHead(); // 构造标准响应头
            service(request, responseHead);
            byte[] responseHeadBiteArray = responseHead.toString().getBytes(WebApplicationServerSetting.CHARSET); // 响应头二进制流
            byte[] responseBodyBiteArray = responseMessageService.readResponseMessageBody(request, responseHead); // 响应体二进制流
            byte[] responseBiteArray = ArrayUtil.ByteArrayMerge(responseHeadBiteArray, responseBodyBiteArray);
            standardWriter.write(responseBiteArray); // 将响应报文头转为字符串后再转为Byte数组，再和响应体合并，最后使用流输出到浏览器
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void service(Request request, ResponseHead responseHead) {
        // 判断协议版本
        if (request.getProtocol().endsWith(HttpProtocol.VERSION)) {
            // 如果HTTP协议版本是1.1
            // 进行HTTP方法(动作)派分，处理
            switch (request.getMethod()) {
                case HttpMethod.METHOD_GET:
                    doGet(request, responseHead);
                    break;
                case HttpMethod.METHOD_POST:
                    doPost(request, responseHead);
                    break;
                case HttpMethod.METHOD_DELETE:
                    doDelete(request, responseHead);
                    break;
                case HttpMethod.METHOD_PATCH:
                    doPatch(request, responseHead);
                    break;
                case HttpMethod.METHOD_PUT:
                    doPut(request, responseHead);
                    break;
                case HttpMethod.METHOD_HEAD:
                    doHead(request, responseHead);
                    break;
                case HttpMethod.METHOD_OPTIONS:
                    doOptions(request, responseHead);
                    break;
                case HttpMethod.METHOD_TRACE:
                    doTrace(request, responseHead);
                    break;
                default:
                    // 如果请求方法不匹配以上所有方法，则报501错误
                    responseHead.setCode(HttpStatusCode.NOT_IMPLEMENTED);
                    responseHead.setStatus(HttpStatusDescription.NOT_IMPLEMENTED);
                    break;
            }
        } else {
            // 如果协议不是HTTP/1.1，则报505错误
            responseHead.setCode(HttpStatusCode.HTTP_VERSION_NOT_SUPPORTED);
            responseHead.setStatus(HttpStatusDescription.HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    private void doGet(Request request, ResponseHead responseHead) {
        // 首先调用转发器可能的请求url转发
        // 再判断转发后的文件类型
        dispatcherService.dispatcher(request);
        String direction = request.getDirection();
        String suffix = FileUtil.getFileSuffix(direction);
        String contentType = ResponseMessageUtil.getContentType(suffix);
        responseHead.setContentType(contentType);
    }

    private void doPost(Request request, ResponseHead responseHead) {
        doGet(request, responseHead);
    }

    private void doDelete(Request request, ResponseHead responseHead) {
        doGet(request, responseHead);
    }

    private void doPatch(Request request, ResponseHead responseHead) {
        doGet(request, responseHead);
    }

    private void doPut(Request request, ResponseHead responseHead) {
        doGet(request, responseHead);
    }

    private void doHead(Request request, ResponseHead responseHead) {
        doGet(request, responseHead);
    }

    private void doOptions(Request request, ResponseHead responseHead) {
        doGet(request, responseHead);
    }

    private void doTrace(Request request, ResponseHead responseHead) {
        doGet(request, responseHead);
    }

}
