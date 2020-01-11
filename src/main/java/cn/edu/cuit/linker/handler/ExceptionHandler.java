package cn.edu.cuit.linker.handler;

import cn.edu.cuit.fatcat.embed.ErrorPage;
import cn.edu.cuit.fatcat.http.HttpContentType;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import cn.edu.cuit.linker.util.FileReader;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Slf4j
public class ExceptionHandler {

    /**
     * 处理异常
     *
     * @param response 响应报文头实体
     */
    public static byte[] handleException(Request request, Response response, Integer code, String status) {
        response.setCode(code);
        response.setStatus(status);
        response.setContentType(HttpContentType.TEXT_HTML);
        byte[] body;
        try {
            // 读取错误页
            body = FileReader.readBinStr(FatcatSetting.ERROR_PAGES.get(response.getCode()));
        } catch (IOException ignore) {
            // 如果ERROR_PAGES中未指定错误页，就用容器自带的错误页
            log.warn("未设置或无法获取错误页: {} - {}", response.getCode(), response.getStatus());
            body = ErrorPage.getTomcatEmbeddedErrorPageBytes(request, response).getBytes(FatcatSetting.CHARSET);
        }
        return body;
    }
}
