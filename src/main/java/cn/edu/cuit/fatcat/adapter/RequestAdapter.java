package cn.edu.cuit.fatcat.adapter;

import cn.edu.cuit.fatcat.http.HttpMethod;
import cn.edu.cuit.fatcat.message.Request;

import java.util.*;

public enum RequestAdapter {
    INSTANCE;

    private static final String[] emptyStringArray = new String[0];

    public Request getRequest(Request request, String context) {
        request.setContext(context);
        String[] requestSpiltHeaderAndBody = context.split("\r\n\r\n", 2);
        String body = "";
        if (requestSpiltHeaderAndBody.length == 2) {
            // 长度为2就说明存在body
            body = requestSpiltHeaderAndBody[1];
        }
        String[] requestHeaderLines = requestSpiltHeaderAndBody[0].split("\r\n"); // 依据换行符拆分Request Headers
        String[] firstLine = requestHeaderLines[0].split(" "); // 根据空格拆分Request报文第一行，能拆出三个子串: 方法(动作) 请求路径[?参数] 协议名/版本号
        request.setMethod(firstLine[0]);
        String[] s = firstLine[1].split("\\?", 2); // 根据问号拆分请求路径，格式为：真路径？参数
        request.setDirection(s[0]);
        String pathVariable = "";
        if (s.length > 1) {
            pathVariable = s[1];
        }
        request.setHeaders(new HashMap<>());
        getParamFromMessage(request.getHeaders(), requestHeaderLines);
        request.setProtocol(firstLine[2]);
        request.setParameters(new HashMap<>());
        if (s.length > 1) {
            getParam(request.getParameters(), pathVariable);
        }
        if (!request.getMethod().equals(HttpMethod.METHOD_GET)) {
            getParam(request.getParameters(), body);
        }
        return request;
    }

    /**
     * 从报文头中分割参数
     * @param message 报文头按行分割
     */
    private void getParamFromMessage(Map<String, Vector<String>> header, String[] message) {
        for (int i = 1; i < message.length; ++i) {
            String[] kv = message[i].split(": ", 2);
            if (kv.length == 2) {
                addParamVector(header, kv[0], kv[1]);
            }
        }
    }

    private void addParamVector(Map<String, Vector<String>> map, String key, String value) {
        Vector<String> storedValue = map.get(key);
        if (storedValue != null) {
            storedValue.add(value);
        } else {
            storedValue = new Vector<>();
            storedValue.add(value);
        }
        map.put(key, storedValue);
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
    private void getParam(Map<String, String[]> parameters, String variable) {
        String[] s = variable.split("&");
        for (String iter : s) {
            String[] kv = iter.split("=", 2);
            if (kv.length == 2) {
                addParamArray(parameters, kv[0], kv[1]);
            }
        }
    }

    private void addParamArray(Map<String, String[]> map, String key, String value) {
        String[] storedArray = map.get(key);
        if (storedArray != null) {
            List<String> list = new ArrayList<>(Arrays.asList(storedArray));
            list.add(value);
            storedArray = new String[storedArray.length + 1];
            for (int i = 0; i < storedArray.length; ++i) {
                storedArray[i] = list.get(i);
            }
            map.put(key, storedArray);
        } else {
            storedArray = new String[1];
            storedArray[0] = value;
        }
        map.put(key, storedArray);
    }

}
