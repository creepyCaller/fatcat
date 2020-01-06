package cn.edu.cuit.linker.service;

import cn.edu.cuit.fatcat.embed.TestPage;
import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.linker.handler.ExceptionHandler;
import cn.edu.cuit.linker.io.Reader;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.ResponseHead;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

/**
 * 响应报文服务
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
@Slf4j
public class ResponseMessageService {

    private Reader standardReader;

    private ExceptionHandler exceptionHandler;

    public ResponseMessageService(Reader standardReader) {
        this.standardReader = standardReader;
        exceptionHandler = new ExceptionHandler(standardReader);
    }

    /**
     * 从文件流读取响应头
     *
     * @return 文件的二进制流
     */
    public byte[] readResponseMessageBody(Request request, ResponseHead responseHead) {
        byte[] body;
        try {
            if (Objects.equals(request.getDirection(), "/TEST.html")) {
                // 如果是调用测试页的话
                body = TestPage.getEmbeddedTestPageBytes(request, responseHead);
            } else {
                // 读出direction路径下的文件
                body = standardReader.read(request.getDirection());
            }
        } catch (FileNotFoundException e) {
            // 404 Not Found
            log.info("找不到文件: {}", request.getDirection());
            body = exceptionHandler.handleException(request, responseHead, HttpStatusCode.NOT_FOUND, HttpStatusDescription.NOT_FOUND);
        } catch (IOException e) {
            // 500 Internal Server Error
            body = exceptionHandler.handleException(request, responseHead, HttpStatusCode.INTERNAL_SERVER_ERROR, HttpStatusDescription.INTERNAL_SERVER_ERROR);
        }
        return body;
    }



}
