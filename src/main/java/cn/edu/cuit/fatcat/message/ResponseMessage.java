package cn.edu.cuit.fatcat.message;

import cn.edu.cuit.fatcat.http.*;
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
public class ResponseMessage {

    // **HEAD**

    private String protocol;

    private Integer code;

    private String status;

    private String connection;

    private String contentType;

    private String contentLanguage;

    // **BODY**

    private String body;

    @Override
    public String toString() {
        return protocol + " " + code + " " + status + "\r\n" +
                "Connection: " + connection + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Language: " + contentLanguage + "\r\n" +
                "\r\n" +
                (body == null ? "" : body);
    }

    // **CONSTRUCTORS**

    public ResponseMessage(Integer code, String status, String contentType, String body) {
        this.code = code;
        this.status = status;
        this.contentType = contentType;
        this.body = body;
    }

    // **STATIC BUILDER**

    public static ResponseMessage standardResponseMessage() {
        return new ResponseMessage(HttpProtocol.HTTP_1_1, HttpStatusCode.OK, HttpStatusDescription.OK, HttpConnection.CLOSE, HttpContentType.TEXT_HTML, HttpContentLanguage.ZH_CN, null);
    }

}
