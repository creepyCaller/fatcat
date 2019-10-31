package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.fatcat.io.Read;
import cn.edu.cuit.fatcat.io.Write;
import cn.edu.cuit.fatcat.message.RequestMessage;
import cn.edu.cuit.fatcat.message.ResponseMessageHead;
import cn.edu.cuit.fatcat.service.RequestMessageService;
import cn.edu.cuit.fatcat.service.ResponseMessageService;
import cn.edu.cuit.fatcat.setting.WebSetting;
import cn.edu.cuit.fatcat.util.ArrayUtil;
import cn.edu.cuit.fatcat.util.FileUtil;
import cn.edu.cuit.fatcat.util.ResponseMessageUtil;
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
public class GeneralServlet implements Runnable {

    private Socket socket;
    private Read read;
    private Write write;
    private RequestMessageService requestMessageService;
    private ResponseMessageService responseMessageService;

    GeneralServlet(Socket socket) {
        this.socket = socket;
    }

    /**
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        RequestMessage requestMessage;
        ResponseMessageHead responseMessageHead;
        byte[] responseMessageBody;
        try {
            init();
            requestMessage = requestMessageService.getRequestMessage(read); // 构造请求报文对象
            responseMessageHead = ResponseMessageHead.standardResponseMessageHead(); // 构造标准响应头
            service(requestMessage, responseMessageHead);
            responseMessageBody = responseMessageService.readResponseMessageBody(requestMessage, responseMessageHead); // 读取响应体二进制流
            write.write(ArrayUtil.BiyeArrayMerge(responseMessageHead.toString().getBytes(WebSetting.CHARSET), responseMessageBody)); // 将响应报文头转为字符串后再转为Byte数组，再和响应体合并，最后使用流输出到浏览器
            destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void service(RequestMessage requestMessage, ResponseMessageHead responseMessageHead) {
        // 判断协议版本
        if (requestMessage.getProtocol().endsWith(HttpProtocol.VERSION)) {
            // 如果HTTP协议版本是1.1
            // 进行HTTP方法(动作)派分，处理
            switch (requestMessage.getMethod()) {
                case HttpMethod.METHOD_GET:
                    doGet(requestMessage, responseMessageHead);
                    break;
                case HttpMethod.METHOD_POST:
                    doPost(requestMessage, responseMessageHead);
                    break;
                case HttpMethod.METHOD_DELETE:
                    doDelete(requestMessage, responseMessageHead);
                    break;
                case HttpMethod.METHOD_PATCH:
                    doPatch(requestMessage, responseMessageHead);
                    break;
                case HttpMethod.METHOD_PUT:
                    doPut(requestMessage, responseMessageHead);
                    break;
                case HttpMethod.METHOD_HEAD:
                    doHead(requestMessage, responseMessageHead);
                    break;
                case HttpMethod.METHOD_OPTIONS:
                    doOptions(requestMessage, responseMessageHead);
                    break;
                case HttpMethod.METHOD_TRACE:
                    doTrace(requestMessage, responseMessageHead);
                    break;
                default:
                    // 如果请求方法不匹配以上所有方法，则报501错误
                    responseMessageHead.setCode(HttpStatusCode.NOT_IMPLEMENTED);
                    responseMessageHead.setStatus(HttpStatusDescription.NOT_IMPLEMENTED);
                    break;
            }
        } else {
            // 如果协议不是HTTP/1.1，则报505错误
            responseMessageHead.setCode(HttpStatusCode.HTTP_VERSION_NOT_SUPPORTED);
            responseMessageHead.setStatus(HttpStatusDescription.HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    private void doGet(RequestMessage requestMessage, ResponseMessageHead responseMessageHead) {
        responseMessageHead.setContentType(ResponseMessageUtil.getContentType(FileUtil.getFileSuffix(requestMessage.getDirection()))); // 从请求报文的路径获取文件后缀名，再使用后缀确定内容类型
    }

    private void doPost(RequestMessage requestMessage, ResponseMessageHead responseMessageHead) {
        doGet(requestMessage, responseMessageHead);
    }

    private void doDelete(RequestMessage requestMessage, ResponseMessageHead responseMessageHead) {
        doGet(requestMessage, responseMessageHead);
    }

    private void doPatch(RequestMessage requestMessage, ResponseMessageHead responseMessageHead) {
        doGet(requestMessage, responseMessageHead);
    }

    private void doPut(RequestMessage requestMessage, ResponseMessageHead responseMessageHead) {
        doGet(requestMessage, responseMessageHead);
    }

    private void doHead(RequestMessage requestMessage, ResponseMessageHead responseMessageHead) {
        doGet(requestMessage, responseMessageHead);
    }

    private void doOptions(RequestMessage requestMessage, ResponseMessageHead responseMessageHead) {
        doGet(requestMessage, responseMessageHead);
    }

    private void doTrace(RequestMessage requestMessage, ResponseMessageHead responseMessageHead) {
        doGet(requestMessage, responseMessageHead);
    }

    private void init() throws IOException {
        read = new Read(socket.getInputStream());
        write = new Write(socket.getOutputStream());
        requestMessageService = new RequestMessageService();
        responseMessageService = new ResponseMessageService(read);
    }

    private void destroy() throws IOException {
        read.close();
        write.close();
        socket.close();
    }

}
