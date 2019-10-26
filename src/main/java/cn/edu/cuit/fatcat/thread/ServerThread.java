package cn.edu.cuit.fatcat.thread;

import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.fatcat.page.ErrorPage;
import cn.edu.cuit.fatcat.message.RequestMessage;
import cn.edu.cuit.fatcat.message.ResponseMessage;
import cn.edu.cuit.fatcat.util.FileUtil;
import cn.edu.cuit.fatcat.util.ResponseMessageUtil;
import cn.hutool.core.util.ArrayUtil;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 对处理一次HTTP请求的子线程
 * 之后要迁移到ServerServlet
 *
 * @author fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
public class ServerThread implements Runnable {

    /* getPath(),getError(),getIndex(),from server.yml,web.yml */
    private static final String WWWROOT = "wwwroot";
    private static final String ERROR_PAGE = "/error/error.html";
    private static final String INDEX = "/index.html";

    private Socket socket;
    private InputStream iStr;
    private OutputStream oStr;
    private byte[]  buffer;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    /**
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        String request;
        String response;
        RequestMessage requestMessage;
        ResponseMessage responseMessage;
        try {
            init();
            request = new String(readRequest(), StandardCharsets.UTF_8); // 读取报文
            requestMessage = getRequestMessage(request); // 构造请求报文对象
            response = generateResponse(requestMessage); // 使用请求报文对象构造响应报文对象
            writeResponse(response); // 调用PrintWriter将响应报文写入浏览器
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从输入流读取请求报文
     *
     * @return 请求报文
     * @throws IOException IO异常
     */
    private byte[] readRequest() throws IOException {
        return read(iStr);
    }

    private byte[] read(InputStream is) throws IOException {
        int length = is.available();
        while (length == 0) {
            length = is.available();
        }
        byte[] buf = new byte[length];
        int read = 0;
        while (read < length) {
            read += is.read(buf, read, length - read);
        }
        buffer = buf;
        return buf;
    }

    /**
     * 由请求报文字符串获取请求报文实体
     * 现在暂时只用得到请求报文的第一行 method + Protocol + Direction
     * 和最后一行
     *
     * @param request 请求报文字符串
     * @return 请求报文实体
     */
    private RequestMessage getRequestMessage(String request) {
        String[] requestLines = request.split("\r\n"); // 依据换行符拆分Request报文
        String[] a = requestLines[0].split(" "); // 根据空格拆分
        String[] s = a[1].split("\\?"); // 根据问号拆分
        String body = "";
        Map<String, String> param = null;
        String path = s[0];
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
        return RequestMessage.construct(a[0], path, a[2], body, param); // 构造请求报文并返回
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

    /**
     * 使用请求报文实体构造响应报文实体
     *
     * @param requestMessage 请求报文实体
     * @return 响应报文实体
     */
    private String generateResponse(RequestMessage requestMessage) {
        // 生成HTTP响应报文头 + 从HTML文件中读取body
        ResponseMessage responseMessage= ResponseMessage.standardResponseMessage();
        // 判断协议版本
        if (requestMessage.getProtocol().equals(HttpProtocol.HTTP_1_1)) {
            // 如果HTTP协议版本是1.1
            // 进行HTTP方法(动作)派分，处理
            // 建议拆出去
            switch (requestMessage.getMethod()) {
                case HttpMethod.METHOD_GET:
                    return doGet(requestMessage);
                case HttpMethod.METHOD_POST:
                    return doPost(requestMessage);
                case HttpMethod.METHOD_DELETE:
                    break;
                case HttpMethod.METHOD_PATCH:
                    break;
                case HttpMethod.METHOD_PUT:
                    break;
                case HttpMethod.METHOD_HEAD:
                    break;
                case HttpMethod.METHOD_OPTIONS:
                    break;
                case HttpMethod.METHOD_TRACE:
                    break;
                default:
                    responseMessage.setCode(HttpStatusCode.NOT_IMPLEMENTED);
                    responseMessage.setStatus(HttpStatusDescription.NOT_IMPLEMENTED);
                    break;
            }
        } else {
            // 如果协议不是HTTP/1.1，则报505错误
            responseMessage.setCode(HttpStatusCode.HTTP_VERSION_NOT_SUPPORTED);
            responseMessage.setStatus(HttpStatusDescription.HTTP_VERSION_NOT_SUPPORTED);
        }
        return responseMessage.toString();
    }

    /**
     * GET
     * 在做其他HTTP方法的时候从里边拆出重复过程
     *
     * @param requestMessage
     * @return
     */
    private String doGet(RequestMessage requestMessage) {
        // 从webapps/{direction}中取出对应文件，输入到ResponseMessage.body中
        ResponseMessage responseMessage = ResponseMessage.standardResponseMessage(); // 生成响应报文
        requestMessage.setDirection(dispatcher(requestMessage.getDirection())); // 进行转发
        try {
            // 读出direction路径下的文件
            // 如果是请求Servlet就要在这里实例化它并且调用、取得返回值
            // 有点小问题
            String body = readContextString(requestMessage.getDirection(), responseMessage.getContentType());
            responseMessage.setContentType(ResponseMessageUtil.getContentType(FileUtil.getFileSuffix(requestMessage.getDirection()))); // 从请求报文的路径获取文件后缀名，再进行内容类型派分
            if (responseMessage.getContentType().equals("text/html")) {
                responseMessage.setBody(body);
            }
        } catch (FileNotFoundException e) {
            // 404 Not Found
            handleException(requestMessage, responseMessage, HttpStatusCode.NOT_FOUND, HttpStatusDescription.NOT_FOUND);
        } catch (IOException e) {
            // IO异常
            handleException(requestMessage, responseMessage, HttpStatusCode.INTERNAL_SERVER_ERROR, HttpStatusDescription.INTERNAL_SERVER_ERROR);
        }
        return responseMessage.toString();
    }

    /**
     * POST，虽然现在看来跟GET一样
     *
     * @param requestMessage
     * @return
     */
    private String doPost(RequestMessage requestMessage) {
        return doGet(requestMessage);
    }

    /**
     * 处理异常
     *
     * @param responseMessage 响应报文实体
     */
    private void handleException(RequestMessage requestMessage, ResponseMessage responseMessage, int code, String status) {
        responseMessage.setCode(code);
        responseMessage.setStatus(status);
        try {
            // 从ERROR_PAGE读取错误页
            String body = readContextString(ERROR_PAGE, HttpContentType.TEXT_HTML);
            responseMessage.setBody(body);
        } catch (IOException ignore) {
            // 如果ERROR_PAGE指定的错误页找不到，就用容器自带的错误页
            responseMessage.setBody(ErrorPage.getEmbeddedErrorPage(responseMessage.getStatus(), responseMessage.getCode(), responseMessage.getStatus(), requestMessage.toString(),responseMessage.toString()));
        }
    }

    /**
     * 读取direction指定的文件
     *
     * @param direction 在WWWROOT下的目标读取文件
     * @return 读取出来的，现在只读网页，所以传出去是String，还没想好图片或者音乐视频怎么办
     * @throws IOException IO异常
     */
    private byte[] readContext(String direction, String type) throws IOException {
        File file = new File(WWWROOT + direction);
        FileInputStream fIStr = new FileInputStream(file);
        return read(fIStr);
    }

    private String readContextString(String direction, String type) throws IOException {
        return new String(readContext(direction, type), StandardCharsets.UTF_8);
    }

    /**
     * 请求路径转发
     *
     * @param direction 待转发的路径
     * @return 转发后的路径或需要调用的Servlet
     */
    private String dispatcher(String direction) {
        // 以后要处理访问Servlet的Request
        // 先找Servlet再找文件？
        // 找到Servlet以后就调用它，最后就把Servlet的Response写到浏览器端
        // 没有对应的Servlet就去WWWROOT下边找文件
        switch (direction) {
            case "/":
                // 如果请求的路径是"/"，则转到规定的主页(现在只是暂时设置为内部属性，以后要在settings里和Servlet里规定)
                return INDEX;
            case "/index":
                return INDEX;
            default:
                return direction;
        }
    }

    /**
     * 将ResponseMessage实体转为字符串，输出到浏览器
     *
     * @param response 响应报文
     */
    private void writeResponse(String response) throws IOException {
        byte[] b = response.getBytes(StandardCharsets.UTF_8);
        if (buffer != null) {
            byte[] c = new byte[b.length + buffer.length];
            System.arraycopy(b, 0, c, 0, b.length);
            System.arraycopy(buffer, 0, c, b.length, buffer.length);
            oStr.write(c);
        } else {
            oStr.write(b);
        }
        oStr.flush();
    }

    /**
     * 初始化
     *
     */
    private void init() throws IOException {
        iStr = socket.getInputStream();
        oStr = socket.getOutputStream();
    }

    /**
     * 将实例交给GC前调用，关闭各种流
     *
     */
    private void close() throws IOException {
        oStr.close();
        iStr.close();
        socket.close();
    }

}
