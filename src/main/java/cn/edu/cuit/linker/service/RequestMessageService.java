package cn.edu.cuit.linker.service;

import cn.edu.cuit.fatcat.http.HttpMethod;
import cn.edu.cuit.linker.io.Reader;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.fatcat.setting.WebApplicationServerSetting;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestMessageService {

    /**
     * 由请求报文字符串获取请求报文实体
     * 现在暂时只用得到请求报文的第一行 method + Direction + Protocol
     *
     * @param standardReader 请求报文字符串
     * @return 请求报文实体
     */
    public Request getRequestMessage(Reader standardReader) throws IOException {
        String request = new String(standardReader.read(), WebApplicationServerSetting.CHARSET);
        String[] requestSpiltHeaderAndBody = request.split("\r\n\r\n");
        String[] requestHeaderLines = requestSpiltHeaderAndBody[0].split("\r\n"); // 依据换行符拆分Request报文
        String body = "";
        if (requestSpiltHeaderAndBody.length > 1) {
            // 长度大于1就说明存在body
            StringBuilder bodySB = new StringBuilder();
            for (int i = 1; i < requestSpiltHeaderAndBody.length; ++i) {
                bodySB.append(requestSpiltHeaderAndBody[i]);
            }
            body = bodySB.toString();
        }
        String[] a = requestHeaderLines[0].split(" "); // 根据空格拆分Request报文第一行，能拆出三个子串: 方法(动作) 请求路径[?参数] 协议名/版本号
        String method = a[0];
        String[] s = a[1].split("\\?"); // 根据问号拆分请求路径，格式为：真路径？参数
        String path = s[0];
        String pathVariable = "";
        if (s.length > 1) {
            pathVariable = s[1];
        }
        Map<String, List<String>> param = new HashMap<>();
        this.getParamFromMessage(param, requestHeaderLines);
        switch (method) {
            // TODO:添加其他HTTP方法、拆分这部分到另一个方法
            case HttpMethod.METHOD_GET:
                if (s.length > 1) {
                    this.getParamFromURL(param, pathVariable);
                }
                break;
            case HttpMethod.METHOD_POST:
                this.getParamFromURL(param, pathVariable);
                this.getParamFromBody(param, body);
                break;
            default:
        }
        return Request.builder()
                .method(a[0])
                .direction(path)
                .protocol(a[2])
                .body(body)
                .param(param)
                .build();
    }

    /**
     * 从报文头中分割参数
     * @param message 报文头按行分割
     */
    private void getParamFromMessage(Map<String, List<String>> param, String[] message) {
        for (int i = 1; i < message.length; ++i) {
            String[] kv = message[i].split(": ");
            if (kv.length >= 2) {
                String key = kv[0];
                StringBuilder valueSB = new StringBuilder();
                for (int j = 1; j < kv.length; ++j) {
                    valueSB.append(kv[j]);
                }
                String value = valueSB.toString();
                this.addParam(param, key, value);
            }
        }
    }

    private void getParamFromBody(Map<String, List<String>> param, String body) {
        this.getParamFromURL(param, body);
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
    private void getParamFromURL(Map<String, List<String>> param, String variable) {
        String[] s = variable.split("&");
        for (String iter : s) {
            String[] a = iter.split("=");
            if (a.length == 2) {
                this.addParam(param, a[0], a[1]);
            }
        }
    }

    private void addParam(Map<String, List<String>> param, String key, String value) {
        List<String> list = param.get(key);
        if (list != null) {
            list.add(value);
        } else {
            list = new ArrayList<>();
            list.add(value);
            param.put(key, list);
        }
    }

}
