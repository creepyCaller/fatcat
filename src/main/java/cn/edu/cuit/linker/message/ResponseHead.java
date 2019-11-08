package cn.edu.cuit.linker.message;

import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.fatcat.setting.WebApplicationServerSetting;
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
public class ResponseHead {

    // **HEAD**

    private String protocol;

    private Integer code;

    private String status;

    private String connection;

    private String contentType;

    private String charSet;

    private String contentLanguage;

    // **Static Builder**

    public static ResponseHead standardResponseMessageHead() {
        return ResponseHead.builder()
                .protocol(HttpProtocol.HTTP_1_1)
                .code(HttpStatusCode.OK)
                .status(HttpStatusDescription.OK)
                .connection(HttpConnection.CLOSE)
                .contentType(HttpContentType.TEXT_HTML)
                .charSet(WebApplicationServerSetting.CHARSET_STRING)
                .contentLanguage(WebApplicationServerSetting.CONTENT_LANGUAGE)
                .build();
    }

    // **ToString**

    @Override
    public String toString() {
        return protocol + " " + code + " " + status + "\r\n" +
                "Connection: " + connection + "\r\n" +
                "Content-Type: " + contentType + (contentType.startsWith("text") ? (charSet == null ? "" : ";charset=" + charSet) : "") + "\r\n" +
                "Content-Language: " + contentLanguage + "\r\n" +
                "\r\n";
    }

}
