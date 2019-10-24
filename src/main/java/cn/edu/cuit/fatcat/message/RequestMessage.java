package cn.edu.cuit.fatcat.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    Map<String, String> param;

    @Override
    public String toString() {
        return method + " " + direction + " " + protocol + "\r\n";
    }

}
