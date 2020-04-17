package cn.edu.cuit.fatcat.handler;

import cn.edu.cuit.fatcat.Setting;
import cn.edu.cuit.fatcat.embed.ErrorPage;
import cn.edu.cuit.fatcat.http.HttpContentType;
import cn.edu.cuit.fatcat.message.Request;
import cn.edu.cuit.fatcat.message.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionHandler {

    /**
     * 处理异常
     *
     * @param response 响应报文头实体
     */
    public static byte[] generateErrorPage(Request request, Response response, Integer code, String status) {
        response.setCode(code);
        response.setStatus(status);
        response.setContentType(HttpContentType.TEXT_HTML);
        response.setCharacterEncoding(Setting.CHARSET_STRING);
        return ErrorPage.getTomcatEmbeddedErrorPage(request, response).getBytes(Setting.CHARSET);
    }
}
