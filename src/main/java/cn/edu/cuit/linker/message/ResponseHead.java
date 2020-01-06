package cn.edu.cuit.linker.message;

import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Date date;

    private Integer contentLength;

    Map<String, List<String>> param;

    // **Static Builder**

    public static ResponseHead standardResponseMessageHead() {
        return ResponseHead.builder()
                .protocol(HttpProtocol.HTTP_1_1)
                .code(HttpStatusCode.OK)
                .status(HttpStatusDescription.OK)
                .connection(HttpConnection.KEEP_ALIVE)
                .contentType(HttpContentType.TEXT_HTML)
                .charSet(FatcatSetting.CHARSET_STRING)
                .date(new Date())
                .param(new HashMap<>())
                .build();
    }

    // **ToString**

    @Override
    public String toString() {
        return protocol + " " + code + " " + status + "\r\n" +
                "Connection: " + connection + "\r\n" +
                "Content-Type: " + contentType + (contentType.startsWith("text") ? (charSet == null ? "" : ";charset=" + charSet) : "") + "\r\n" +
                "Date: " + date.toString() + "\r\n" +
                "Server: Fatcat\r\n" +
                this.getParamString() +
                "\r\n";
    }

    private String getParamString() {
        if (param != null) {
            StringBuilder paramString = new StringBuilder();
            param.forEach((key, value) -> paramString.append(key).append(": ").append(value.toString()).append("\r\n"));
            return paramString.toString();
        }
        return "";
    }

}
