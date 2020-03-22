package cn.edu.cuit.linker.adapter;

import cn.edu.cuit.linker.message.Request;
import java.util.*;

public class RequestAdapter {

    public Request getRequest(String request) {
        String[] requestSpiltHeaderAndBody = request.split("\r\n\r\n", 2);
        String body = "";
        if (requestSpiltHeaderAndBody.length == 2) {
            // 长度为2就说明存在body
            body = requestSpiltHeaderAndBody[1];
        }
        String[] requestHeaderLines = requestSpiltHeaderAndBody[0].split("\r\n"); // 依据换行符拆分Request Headers
        String[] firstLine = requestHeaderLines[0].split(" "); // 根据空格拆分Request报文第一行，能拆出三个子串: 方法(动作) 请求路径[?参数] 协议名/版本号
        String method = firstLine[0];
        String[] s = firstLine[1].split("\\?", 2); // 根据问号拆分请求路径，格式为：真路径？参数
        String path = s[0];
        String pathVariable = "";
        if (s.length > 1) {
            pathVariable = s[1];
        }
        Map<String, List<String>> header = new HashMap<>();
        this.getParamFromMessage(header, requestHeaderLines);
        String protocol = firstLine[2];
        if (s.length > 1) {
            this.getParamFromURL(header, pathVariable);
        }
        this.getParamFromBody(header, body);
        return Request.builder()
                .method(method)
                .direction(path)
                .protocol(protocol)
                .body(body)
                .header(header)
                .context(request)
                .build();
    }

    /**
     * 从报文头中分割参数
     * @param message 报文头按行分割
     */
    private void getParamFromMessage(Map<String, List<String>> header, String[] message) {
        for (int i = 1; i < message.length; ++i) {
            String[] kv = message[i].split(": ", 2);
            if (kv.length == 2) {
                this.addParam(header, kv[0], kv[1]);
            }
        }
    }

    private void getParamFromBody(Map<String, List<String>> header, String body) {
        this.getParamFromURL(header, body);
    }

    /**
     * 从参数团中分割出参数
     * 示例：username=admin&password=123456&remember=true
     * 结果：Map<>[0] = <"username", "admin">
     *      Map<>[1] = <"password", "123456">
     *      Map<>[2] = <"remember", "true">
     *
     * @param variable 参数团
     */
    private void getParamFromURL(Map<String, List<String>> header, String variable) {
        String[] s = variable.split("&");
        for (String iter : s) {
            String[] kv = iter.split("=", 2);
            if (kv.length == 2) {
                this.addParam(header, kv[0], kv[1]);
            } else if (kv.length == 1) {
                this.addParam(header, kv[0], "");
            }
        }
    }

    private void addParam(Map<String, List<String>> header, String key, String value) {
        List<String> list = header.get(key);
        if (list != null) {
            list.add(value);
        } else {
            list = new ArrayList<>();
            list.add(value);
            header.put(key, list);
        }
    }

}
