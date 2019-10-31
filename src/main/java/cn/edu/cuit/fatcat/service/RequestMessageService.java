package cn.edu.cuit.fatcat.service;

import cn.edu.cuit.fatcat.http.HttpMethod;
import cn.edu.cuit.fatcat.http.RequestType;
import cn.edu.cuit.fatcat.io.Read;
import cn.edu.cuit.fatcat.message.RequestMessage;
import cn.edu.cuit.fatcat.setting.WebSetting;
import cn.edu.cuit.fatcat.util.FileUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestMessageService {

    /**
     * 由请求报文字符串获取请求报文实体
     * 现在暂时只用得到请求报文的第一行 method + Direction + Protocol
     * 和最后一行
     *
     * @param read 请求报文字符串
     * @return 请求报文实体
     */
    public RequestMessage getRequestMessage(Read read) throws IOException {
        String request = new String(read.read(), WebSetting.CHARSET);
        String[] requestLines = request.split("\r\n"); // 依据换行符拆分Request报文
        String[] a = requestLines[0].split(" "); // 根据空格拆分Request报文第一行，能拆出三个子串: 方法(动作) 请求路径[?参数] 协议名/版本号
        String[] s = a[1].split("\\?"); // 根据问号拆分请求路径，格式为：真路径？参数
        String path = s[0];
        String body = ""; // 如果是POST方法(或者是文件，现在暂时不考虑)，需要使用流来读取body
        Map<String, String> param = null;
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
        String requestType = getRequestType(path);
        String servletName;
        if (RequestType.SERVLET.equals(requestType)) {
            servletName = getServletName(path);
        } else {
            servletName = "";
        }
        return RequestMessage.builder()
                .method(a[0])
                .direction(path)
                .protocol(a[2])
                .requestType(requestType)
                .servletName(servletName)
                .body(body)
                .param(param)
                .build();
    }

    /**
     * 确认请求的类型，是请求一个文件还是请求servlet
     * 如果请求没有后缀名就假设为Servlet，事实上应该查表或者是查注解，现在只是测试
     * 如果有请求路径有后缀名，则被认为是文件，没有则先检查有没有设置转发
     * 最后再确认是Servlet
     * 事实上应该先检查此路径有没有映射到某个Servlet
     *
     * @param path 请求路径
     * @return 请求类型
     */
    private String getRequestType(String path) {
        if ("/".equals(path)) {
            return RequestType.DISPATCH;
        } else if ("".equals(FileUtil.getFileSuffix(path))) {
            return RequestType.SERVLET;
        }
        return RequestType.FILE;
    }

    /**
     * 从path中获取应该调用的Servlet的实例
     *
     * @param path 请求路径
     * @return 路径请求到的Servlet的名字
     */
    private String getServletName(String path) {
        return path;
    }

    /**
     * 从参数团中分割出参数
     * 示例：username=admin&password=123456&remember=
     * 结果：Map<>[0] = <"username", "admin">
     *      Map<>[1] = <"password", "123456">
     *
     * @param variable 参数团
     * @return 参数的键值对的数组
     */
    private Map<String, String> getParam(String variable) {
        String[] s = variable.split("&");
        Map<String, String> param = new HashMap<>();
        for (String iter : s) {
            String[] a = iter.split("=");
            if (a.length == 2) {
                param.put(a[0], a[1]);
            }
        }
        return param;
    }

}
