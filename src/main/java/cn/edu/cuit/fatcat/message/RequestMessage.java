package cn.edu.cuit.fatcat.message;

import lombok.*;
import java.util.Map;

/**
 * 请求报文实体
 *
 * @author fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessage {

    // **HEAD**

    private String method;

    private String direction;

    private String protocol;

    // **PROPERTIES**

    private String RequestType;

    public String servletName;

    // **BODY**

    private String body;

    @Override
    public String toString() {
        return method + " " + direction + " " + protocol +
               "\r\n" +
               (body == null ? "" : body);
    }

    // **PARAMS**

    Map<String, String> param;

    // **STATIC BUILDER**

    public static RequestMessage empty() {
        return new RequestMessage("", "", "", "", "", "", null);
    }

    public static RequestMessage construct(String method, String direction, String protocol, String requestType, String servletName, String body, Map<String, String> param) {
        return new RequestMessage(method, direction, protocol, requestType, servletName, body, param);
    }

}
