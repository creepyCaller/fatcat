package cn.edu.cuit.fatcat.message;

import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.fatcat.setting.WebSetting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应报文实体
 *
 * @author fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessageHead {

    // **HEAD**

    private String protocol;

    private Integer code;

    private String status;

    private String connection;

    private String contentType;

    private String charSet;

    private String contentLanguage;

    @Override
    public String toString() {
        return protocol + " " + code + " " + status + "\r\n" +
                "Connection: " + connection + "\r\n" +
                "Content-Type: " + contentType + (contentType.startsWith("text") ? (charSet == null ? "" : ";charset=" + charSet) : "") + "\r\n" +
                "Content-Language: " + contentLanguage + "\r\n" +
                "\r\n";
    }

    // **STATIC BUILDER**

    public static ResponseMessageHead standardResponseMessageHead() {
        return new ResponseMessageHead(HttpProtocol.HTTP_1_1, HttpStatusCode.OK, HttpStatusDescription.OK, HttpConnection.CLOSE, HttpContentType.TEXT_HTML, WebSetting.CHARSET_STRING, WebSetting.CONTENT_LANGUAGE);
    }

}
