package cn.edu.cuit.linker.handler;

import cn.edu.cuit.fatcat.embed.ErrorPage;
import cn.edu.cuit.fatcat.http.HttpContentType;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import cn.edu.cuit.linker.io.Reader;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.ResponseHead;

import java.io.IOException;

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
            // 如果ERROR_PAGE指定的错误页找不到，就用容器自带的错误页
            body = ErrorPage.getTomcatEmbeddedErrorPageBytes(request, responseHead);
        }
        return body;
    }
}
