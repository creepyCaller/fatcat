package cn.edu.cuit.fatcat.adapter;

import cn.edu.cuit.fatcat.Dispatcher;
import cn.edu.cuit.fatcat.http.HttpMethod;
import cn.edu.cuit.fatcat.message.Request;

import java.util.*;

// TODO: 使用惰性转换优化！
// TODO: 使用异步适配优化!
public enum RequestAdapter {
    INSTANCE;

    public void setRequest(Request request, String context) {
        request.setContext(context);
        String[] requestSpiltHeaderAndBody = context.split("\r\n\r\n", 2); // 拆分头和body
        String body = "";
        if (requestSpiltHeaderAndBody.length == 2) {
            // 长度为2就说明存在body
            body = requestSpiltHeaderAndBody[1];
        }
        String[] requestHeaderLines = requestSpiltHeaderAndBody[0].split("\r\n"); // 依据换行符拆分Request Headers
        String[] statusLine = requestHeaderLines[0].split(" "); // 根据空格拆分Request报文第一行，能拆出三个子串: 方法(动作) 请求路径[?参数] 协议名/版本号
        request.setMethod(statusLine[0]);
        String[] requestDirectionDiv = statusLine[1].split("\\?", 2); // 根据问号拆分请求路径，格式为：真路径？参数
        request.setProtocol(statusLine[2]); // 设置协议
        request.setDirection(requestDirectionDiv[0]); // 设置请求路径
        request.setDispatchedDirection(Dispatcher.INSTANCE.dispatch(request.getDirection())); // 处理转发
        String pathVariables = "";
        if (requestDirectionDiv.length > 1) {
            pathVariables = requestDirectionDiv[1];
        }
        request.setHeaders(new HashMap<>());
        getParamFromMessage(request.getHeaders(), requestHeaderLines); // 初始化Header
        request.setParameters(new HashMap<>());
        if (requestDirectionDiv.length > 1) {
            getParam(request.getParameters(), pathVariables); // 如果请求路径包含了参数
        }
        if (!HttpMethod.METHOD_GET.equals(request.getMethod())) {
            getParam(request.getParameters(), body); // 如果请求类型不是Get就找body里的参数
        }
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
     * 示例：username=admin&password=123456&remember=true&ThisIsAString
     * 结果：Map<>[0] = <"username", "admin">
     *      Map<>[1] = <"password", "123456">
     *      Map<>[2] = <"remember", "true">
     *      Map<>[3] = <"ThisIsAString", "">
     *
     * @param variable 参数团
     */
    private void getParam(Map<String, String[]> parameters, String variable) {
        String[] s = variable.split("&");
        for (String iter : s) {
            String[] kv = iter.split("=", 2);
            if (kv.length == 1) {
                addParamArray(parameters, kv[0], "");
            }
            if (kv.length == 2) {
                addParamArray(parameters, kv[0], kv[1]);
            }
        }
    }

    private void addParamArray(Map<String, String[]> map, String key, String value) {
        String[] storedArray = map.get(key);
        if (storedArray != null) {
            String[] newArray = new String[storedArray.length+ 1];
            System.arraycopy(storedArray, 0, newArray, 0, storedArray.length);
            newArray[storedArray.length] = value;
            map.put(key, newArray);
        } else {
            storedArray = new String[1];
            storedArray[0] = value;
            map.put(key, storedArray);
        }
    }

}
