package cn.edu.cuit.fatcat.adapter;

import cn.edu.cuit.fatcat.http.HttpHeader;
import cn.edu.cuit.fatcat.message.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * 响应报文适配器
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
@Slf4j
public enum ResponseAdapter {
    INSTANCE;

    private String getResponseFirstLine(Response response) {
        return response.getProtocol() + " " + response.getCode() + " " + response.getStatusMessage() + "\r\n";
    }

    private String getResponseHeadersLine(Response response) {
        if (response.getMapHeaders() != null) {
            StringBuilder paramString = new StringBuilder();
            response.getMapHeaders().forEach((key, value) -> value.forEach(each -> paramString.append(key).append(": ").append(each).append("\r\n")));
            return paramString.toString();
        }
        return "";
    }

    public String getResponseHead(Response response) {
        if (response.getHeader("Server") == null) {
            response.setHeader("Server", "FatCat/0.2");
        }
        if (response.getHeader("Data") == null) {
            response.setDateHeader("Data", System.currentTimeMillis());
        }
        if (response.getHeader(HttpHeader.CONNECTION) == null) {
            response.setHeader(HttpHeader.CONNECTION, "keep-alive");
        }
        return getResponseFirstLine(response) + getResponseHeadersLine(response) + "\r\n";
    }

}
