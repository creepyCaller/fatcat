package cn.edu.cuit.linker.message;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    // **PARAMS**

    private Map<String, List<String>> header;

    // **BODY**

    private String body;

    // **ToString**

    private String context;

    @Override
    public String toString() {
        return context;
    }

    public String getParamString() {
        if (header != null) {
            StringBuilder paramString = new StringBuilder();
            header.forEach((key, value) -> paramString.append(key).append(": ").append(value.toString()).append("\r\n"));
            return paramString.toString();
        }
        return "";
    }
}
