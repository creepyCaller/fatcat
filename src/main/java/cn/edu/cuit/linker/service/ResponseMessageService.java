package cn.edu.cuit.linker.service;

import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.linker.io.Reader;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.ResponseHead;
import cn.edu.cuit.fatcat.embed.ErrorPage;
import cn.edu.cuit.fatcat.setting.WebApplicationServerSetting;

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


    private Reader standardReader;

    public ResponseMessageService(Reader standardReader) {
        this.standardReader = standardReader;
    }

    /**
     * 从文件流读取响应头
     *
     * @return 文件的二进制流
     */
    public byte[] readResponseMessageBody(Request request, ResponseHead responseHead) {
        byte[] body;
        try {
            // 读出direction路径下的文件
            body = standardReader.read(request.getDirection());
        } catch (FileNotFoundException e) {
            // 404 Not Found
            body = handleException(request, responseHead, HttpStatusCode.NOT_FOUND, HttpStatusDescription.NOT_FOUND);
        } catch (IOException e) {
            // 500 Internal Server Error
            body = handleException(request, responseHead, HttpStatusCode.INTERNAL_SERVER_ERROR, HttpStatusDescription.INTERNAL_SERVER_ERROR);
        }
        return body;
    }

    /**
     * 处理异常
     *
     * @param responseHead 响应报文头实体
     */
    private byte[] handleException(Request request, ResponseHead responseHead, int code, String status) {
        responseHead.setCode(code);
        responseHead.setStatus(status);
        responseHead.setContentType(HttpContentType.TEXT_HTML);
        byte[] body;
        try {
            // 从ERROR_PAGE读取错误页
            body = standardReader.read(WebApplicationServerSetting.ERROR_PAGE);
        } catch (IOException ignore) {
            // 如果ERROR_PAGE指定的错误页找不到，就用容器自带的错误页
            body = ErrorPage.getEmbeddedErrorPageBytes(request, responseHead);
        }
        return body;
    }

}
