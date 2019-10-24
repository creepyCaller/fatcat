package cn.edu.cuit.fatcat.thread;

import cn.edu.cuit.fatcat.html.ErrorPages;
import cn.edu.cuit.fatcat.http.HttpMethod;
import cn.edu.cuit.fatcat.http.HttpProtocol;
import cn.edu.cuit.fatcat.http.HttpStatusCode;
import cn.edu.cuit.fatcat.http.HttpStatusDescription;
import cn.edu.cuit.fatcat.message.RequestMessage;
import cn.edu.cuit.fatcat.message.ResponseMessage;
import cn.edu.cuit.fatcat.util.FileUtil;
import cn.edu.cuit.fatcat.util.ResponseMessageUtil;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 对处理一次HTTP请求的子线程
 *
 * @author fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
public class ServerThread implements Runnable {
    private static final String PATH = "wwwroot";
    private static final String ERROR = "/error/error.html";
    private static final String INDEX = "/index.html";

    private Socket socket;
    private BufferedReader bR;
    private PrintWriter writer;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    /**
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        RequestMessage requestMessage = null;
        ResponseMessage responseMessage = null;
        try {
            init();
            requestMessage = getRequestMessage(readRequest()); // 构造请求报文对象
            responseMessage = generateResponse(requestMessage); // 使用请求报文对象构造响应报文对象
            writeResponse(responseMessage); // 调用PrintWriter将响应报文写入浏览器
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取请求报文
     *
     * @return 请求报文
     * @throws IOException IO异常
     */
    private String readRequest() throws IOException {
        StringBuilder sb = new StringBuilder();
        String str = null;
        while (!(str = bR.readLine()).equals("")) {
            sb.append(str).append("\r\n");
        }
        return sb.toString();
    }

    /**
     * 由请求报文字符串获取请求报文实体
     * 现在暂时只用得到请求报文的第一行 method + Protocol + Direction
     *
     * @param request 请求报文字符串
     * @return 请求报文实体
     */
    private RequestMessage getRequestMessage(String request) {
        String[] requestLines = request.split("\r\n"); // 依据换行符拆分Request报文
        String[] a = requestLines[0].split(" "); // 根据空格拆分
        String[] s = a[1].split("\\?"); // 从问号一切为二
        String path = "";
        String variable = "";
        if (s.length == 1) {
            // 如果direction没有传参
            path = s[0];
        } else {
            path = s[0];
            variable = s[1];
        }
        Map<String, String> param = getParam(variable);
        return new RequestMessage(a[0], path, a[2], param); // 构造请求报文并返回
    }

    /**
     * 从参数团中分割出参数
     * 示例：username=admin&password=123456&remember=
     * 结果：Map<>[0] = <"username", "admin">
     *      Map<>[1] = <"password", "123456">
     *      Map<>[2] = <"remember", null>
     *
     * @param variable 参数团
     * @return 参数的键值对的数组
     */
    private Map<String, String> getParam(String variable) {
        String[] s = variable.split("&");
        Map<String, String> param = new HashMap<>();
        for (String iter : s) {
            String[] a = iter.split("=");
            if (a.length == 1) {
                // 如果等号右边没有值，则为空串(或者是null？)
                param.put(a[0], "");
            } else {
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
    private ResponseMessage generateResponse(RequestMessage requestMessage) {
        // 生成HTTP响应报文头 + 从HTML文件中读取body
        ResponseMessage responseMessage = ResponseMessage.standardResponseMessage();
        // 判断协议版本
        if (requestMessage.getProtocol().equals(HttpProtocol.HTTP_1_1)) {
            // 如果HTTP协议版本是1.1
            // 进行HTTP方法(动作)派分，处理
            // 建议拆出去
            switch (requestMessage.getMethod()) {
                case HttpMethod.METHOD_GET:
                    // GET方法
                    responseMessage = doGet(requestMessage);
                    break;
                case HttpMethod.METHOD_POST:

                    break;
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
                    // 不明动作
                    responseMessage.setCode(HttpStatusCode.NOT_IMPLEMENTED);
                    responseMessage.setStatus(HttpStatusDescription.NOT_IMPLEMENTED);
                    break;
            }
        } else {
            // 如果协议不是HTTP/1.1，则报505错误
            responseMessage.setCode(HttpStatusCode.HTTP_VERSION_NOT_SUPPORTED);
            responseMessage.setStatus(HttpStatusDescription.HTTP_VERSION_NOT_SUPPORTED);
        }
        return responseMessage;
    }

    // 在做其他HTTP方法的时候从里边拆出重复过程
    private ResponseMessage doGet(RequestMessage requestMessage) {
        // 从webapps/{direction}中取出对应文件，输入到ResponseMessage.body中
        ResponseMessage responseMessage = ResponseMessage.standardResponseMessage();
        String direction = dispatcher(requestMessage.getDirection()); // 进行转发
        responseMessage.setContentType(ResponseMessageUtil.getContentType(FileUtil.getFileSuffix(direction))); // 从请求报文的路径获取文件后缀名，再进行内容类型派分
        try {
            String body = readContext(direction);
            responseMessage.setBody(body);
        } catch (FileNotFoundException e) {
            // 404 Not Found
            handlerException(requestMessage, responseMessage, HttpStatusCode.NOT_FOUND, HttpStatusDescription.NOT_FOUND);
            e.printStackTrace();
        } catch (IOException e) {
            // IO异常
            handlerException(requestMessage, responseMessage, HttpStatusCode.INTERNAL_SERVER_ERROR, HttpStatusDescription.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
        return responseMessage;
    }

    /**
     * 处理异常
     *
     * @param responseMessage 响应报文实体
     */
    private void handlerException(RequestMessage requestMessage, ResponseMessage responseMessage, int code, String status) {
        responseMessage.setCode(code);
        responseMessage.setStatus(status);
        if (responseMessage.getContentType().equals("text/html")) {
            // 如果请求的是一个html文件，就在响应体中加入网页
            try {
                // 从ERROR读取错误页
                String body = readContext(ERROR);
                responseMessage.setBody(body);
            } catch (IOException ignore) {
                // 如果ERROR指定的错误页找不到，就用容器自带的错误页
                responseMessage.setBody(ErrorPages.getEmbeddedErrorPage(responseMessage.getStatus(), responseMessage.getCode(), responseMessage.getStatus(), requestMessage.toString(),responseMessage.toString()));
            }
        } else {
            // 如果是其他资源就不加(我也不知道该怎么操作才是对的)
            responseMessage.setBody(null);
        }
    }

    /**
     * 读取direction指定的文件
     *
     * @param direction 在PATH下的目标读取文件
     * @return 读取出来的，现在只读网页，所以传出去是String，还没想好图片或者音乐视频怎么办
     * @throws IOException IO异常
     */
    private String readContext(String direction) throws IOException {
        File file = new File(PATH + direction);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String str = null;
        // 如果是图片该怎么传?
        while ((str = reader.readLine()) != null) {
            sb.append(str).append("\r\n");
        }
        return sb.toString();
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
        // 没有对应的Servlet就去PATH下边找文件
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
     * @param responseMessage 响应报文实体
     */
    private void writeResponse(ResponseMessage responseMessage) {
        writer.print(responseMessage.toString());
        writer.flush();
    }

    /**
     * 初始化
     *
     */
    private void init() throws IOException {
        bR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * 将实例交给GC前调用，关闭各种流
     *
     */
    private void close() throws IOException {
        writer.close();
        bR.close();
        socket.close();
    }

}
