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
        String[] requestLines = request.split("\r\n"); // 依据换行符拆分Request报文
        String[] a = requestLines[0].split(" "); // 根据空格拆分Request报文第一行，能拆出三个子串: 方法(动作) 请求路径[?参数] 协议名/版本号
        String[] s = a[1].split("\\?"); // 根据问号拆分请求路径，格式为：真路径？参数
        String path = s[0];
        String body = null;  // 需要改为使用比特流来装载body
        Map<String, List<String>> param = null;
        switch (a[0]) {
            case HttpMethod.METHOD_GET:
                if (s.length > 1) {
                    param = getParam(s[1]);
                }
                break;
            case HttpMethod.METHOD_POST:
                body = requestLines[requestLines.length - 1];
                param = getParam(body);
                break;
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
     * 从参数团中分割出参数
     * 示例：username=admin&password=123456&remember=true
     * 结果：Map<>[0] = <"username", "admin">
     *      Map<>[1] = <"password", "123456">
     *      Map<>[2] = <"remember", "true">
     *
     * @param variable 参数团
     * @return 参数的键值对的数组
     */
    private Map<String, List<String>> getParam(String variable) {
        String[] s = variable.split("&");
        Map<String, List<String>> param = new HashMap<>();
        for (String iter : s) {
            String[] a = iter.split("=");
            if (a.length == 2) {
                this.addParam(param, a[0], a[1]);
            }
        }
        return param;
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
