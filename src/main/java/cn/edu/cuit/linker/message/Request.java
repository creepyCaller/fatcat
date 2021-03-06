package cn.edu.cuit.linker.message;

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
public class Request {

    // **HEAD**

    private String method;

    private String direction;

    private String protocol;

    // **BODY**

    private String body;

    // **Params**

    Map<String, String> param;

    // **ToString**

    @Override
    public String toString() {
        return method + " " + direction + " " + protocol +
                "\r\n" +
                (body == null ? "" : body);
    }

}
