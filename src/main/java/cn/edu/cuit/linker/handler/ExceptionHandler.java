package cn.edu.cuit.linker.handler;

import cn.edu.cuit.fatcat.embed.ErrorPage;
import cn.edu.cuit.fatcat.http.HttpContentType;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import cn.edu.cuit.linker.io.Reader;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.ResponseHead;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Slf4j
public class ExceptionHandler {

    private Reader standardReader;

    public ExceptionHandler(Reader standardReader) {
        this.standardReader = standardReader;
    }

    /**
     * 处理异常
     *
     * @param responseHead 响应报文头实体
     */
    public byte[] handleException(Request request, ResponseHead responseHead, Integer code, String status) {
        responseHead.setCode(code);
        responseHead.setStatus(status);
        responseHead.setContentType(HttpContentType.TEXT_HTML);
        byte[] body;
        try {
            // 读取错误页
            body = standardReader.read(FatcatSetting.ERROR_PAGES.get(responseHead.getCode()));
        } catch (IOException ignore) {
            // 如果ERROR_PAGES中未指定错误页，就用容器自带的错误页
            log.warn("未设置或无法获取错误页: {} - {}", responseHead.getCode(), responseHead.getStatus());
            body = ErrorPage.getTomcatEmbeddedErrorPageBytes(request, responseHead);
        }
        return body;
    }
}
