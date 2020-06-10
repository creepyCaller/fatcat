package cn.edu.cuit.fatcat.adapter;

import cn.edu.cuit.fatcat.http.HttpHeader;
import cn.edu.cuit.fatcat.message.Response;
import cn.edu.cuit.fatcat.util.FastHttpDateFormat;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import java.util.List;

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
            StringBuilder sb = new StringBuilder();
            response.getMapHeaders()
                    .forEach((key, value) -> value
                            .forEach(each ->
                                    sb.append(key).append(": ").append(each).append("\r\n")));
            return sb.toString();
        }
        return "";
    }

    private String getResponseCookiesLine(Response response) {
        if (response.getCookies() != null) {
            StringBuilder sb = new StringBuilder();
            response.getCookies().forEach((cookie) -> {
                // Set-Cookie:
                sb.append(HttpHeader.SET_COOKIE).append(": ");
                // key=value
                sb.append(cookie.getName()).append("=").append(cookie.getValue());
                if (cookie.getPath() != null) {
                    // ; path=/
                    sb.append("; path=").append(cookie.getPath());
                }
                if (cookie.getDomain() != null) {
                    //; domain=www.example.com
                    sb.append("; domain=").append(cookie.getDomain());
                }
                if (cookie.getMaxAge() != -1) {
                    // ; expires=Sun, 19-Apr-2020 09:24:36 GMT
                    long maxAgeMillis = cookie.getMaxAge() * 1000L;
                    sb.append("; expires=").append(FastHttpDateFormat.formatDate(System.currentTimeMillis() + maxAgeMillis));
                }
                if (cookie.getComment() != null) {
                    // ; comment=purpose
                    sb.append("; comment=").append(cookie.getComment());
                }
                if (cookie.getVersion() != 0) {
                    // ; version=0
                    sb.append("; comment=").append(cookie.getVersion());
                }
                if (cookie.getSecure()) {
                    // ; secure
                    sb.append("; secure");
                }
                if (cookie.isHttpOnly()) {
                    // ; HttpOnly
                    sb.append("; HttpOnly");
                }
                sb.append("\r\n");
            });
            return sb.toString();
        }
        return "";
    }

    public String getResponseHead(Response response) {
        if (response.getHeader("Server") == null) {
            response.setHeader("Server", "FatCat");
        }
        if (response.getHeader("Data") == null) {
            response.setDateHeader("Data", System.currentTimeMillis());
        }
        if (response.getHeader(HttpHeader.CONNECTION) == null) {
            response.setHeader(HttpHeader.CONNECTION, "keep-alive");
        }
        return getResponseFirstLine(response) + getResponseHeadersLine(response) + getResponseCookiesLine(response) + "\r\n";
    }

}
