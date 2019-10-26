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
// **PROPERTIES**

    private String method;

    private String direction;

    private String protocol;

    private String body;

    Map<String, String> param;

    public String getMessage() {
        return method + " " + direction + " " + protocol + "\r\n" + body;
    }

    public static RequestMessage empty() {
        return new RequestMessage("", "", "", "", null);
    }

    public static RequestMessage construct(String method, String direction, String protocol, String body, Map<String, String> param) {
        return new RequestMessage(method, direction, protocol, body, param);
    }

}
