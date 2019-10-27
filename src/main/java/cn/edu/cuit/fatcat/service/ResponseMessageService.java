package cn.edu.cuit.fatcat.service;

import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.fatcat.io.Read;
import cn.edu.cuit.fatcat.message.RequestMessage;
import cn.edu.cuit.fatcat.message.ResponseMessageHead;
import cn.edu.cuit.fatcat.page.ErrorPage;
import cn.edu.cuit.fatcat.setting.Web;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 响应报文服务
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
public class ResponseMessageService {

    private DispatcherService dispatcherService;
    private Read read;

    public ResponseMessageService(Read read) {
        dispatcherService = new DispatcherService();
        this.read = read;
    }

    /**
     * 从文件流读取响应头
     *
     * @return 文件的二进制流
     */
    public byte[] readResponseMessageBody(RequestMessage requestMessage, ResponseMessageHead responseMessageHead) {
        byte[] body;
        try {
            // 读出direction路径下的文件
            // 首先判断是否为servlet，默认为文件
            // 如果是请求Servlet就要在这里实例化它并且调用、取得返回值
            switch(requestMessage.getRequestType()) {
                case RequestType.FILE:
                    body = read.read(requestMessage.getDirection());
                    break;
                case RequestType.DISPATCH:
                    body = read.read(dispatcherService.dispatcher(requestMessage.getDirection()));
                    break;
                case RequestType.SERVLET:
                    body = servlet(requestMessage, responseMessageHead);
                    break;
                default:
                    body = handleException(requestMessage, responseMessageHead, HttpStatusCode.INTERNAL_SERVER_ERROR, HttpStatusDescription.INTERNAL_SERVER_ERROR);
            }
        } catch (FileNotFoundException e) {
            // 404 Not Found
            body = handleException(requestMessage, responseMessageHead, HttpStatusCode.NOT_FOUND, HttpStatusDescription.NOT_FOUND);
        } catch (IOException e) {
            // IO异常
            body = handleException(requestMessage, responseMessageHead, HttpStatusCode.INTERNAL_SERVER_ERROR, HttpStatusDescription.INTERNAL_SERVER_ERROR);
        }
        return body;
    }

    /**
     * Dummy Servlet
     *
     */
    private byte[] servlet(RequestMessage requestMessage, ResponseMessageHead responseMessageHead) {
        responseMessageHead.setCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
        responseMessageHead.setStatus(HttpStatusDescription.INTERNAL_SERVER_ERROR);
        return ErrorPage.getEmbeddedErrorPageBytes("暂未支持Servlet", responseMessageHead.getCode(), responseMessageHead.getStatus(), requestMessage.toString(), responseMessageHead.toString());
    }

    /**
     * 处理异常
     *
     * @param responseMessageHead 响应报文头实体
     */
    private byte[] handleException(RequestMessage requestMessage, ResponseMessageHead responseMessageHead, int code, String status) {
        responseMessageHead.setCode(code);
        responseMessageHead.setStatus(status);
        byte[] body;
        try {
            // 从ERROR_PAGE读取错误页
            body = read.read(Web.ERROR_PAGE);
        } catch (IOException ignore) {
            // 如果ERROR_PAGE指定的错误页找不到，就用容器自带的错误页
            body = ErrorPage.getEmbeddedErrorPageBytes(responseMessageHead.getStatus(), responseMessageHead.getCode(), responseMessageHead.getStatus(), requestMessage.toString(), responseMessageHead.toString());
        }
        return body;
    }

}
