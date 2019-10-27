package cn.edu.cuit.fatcat.message;

import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.fatcat.setting.Web;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.nio.charset.Charset;

/**
 * 响应报文实体
 *
 * @author fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Data
@Builder
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
                "Content-Type: " + contentType + (charSet == null ? "" : ";charset=" + charSet) + "\r\n" +
                "Content-Language: " + contentLanguage + "\r\n" +
                "\r\n";
    }

    // **CONSTRUCTORS**

    private ResponseMessageHead(String protocol, Integer code, String status, String connection, String contentType, String charSet, String contentLanguage) {
        this.protocol = protocol;
        this.code = code;
        this.status = status;
        this.connection = connection;
        this.contentType = contentType;
        this.charSet = charSet;
        this.contentLanguage = contentLanguage;
    }

    // **STATIC BUILDER**

    public static ResponseMessageHead standardResponseMessageHead() {
        return new ResponseMessageHead(HttpProtocol.HTTP_1_1, HttpStatusCode.OK, HttpStatusDescription.OK, HttpConnection.CLOSE, HttpContentType.TEXT_HTML, Web.CHARSET_STRING, Web.CONTENT_LANGUAGE);
    }

}
