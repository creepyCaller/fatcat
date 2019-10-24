package cn.edu.cuit.fatcat.util;

/**
 * 响应报文工具类，处理响应报文相关
 *
 * @author fpc
 * @date 2019/10/24
 * @since Fatcat 0.0.1
 */
public class ResponseMessageUtil {

    /**
     * 根据文件后缀名返回响应内容类型
     *
     * @param subffix 文件后缀名
     * @return 响应内容类型
     */
    public static String getContentType(String subffix) {
        switch (subffix) {
            case "html":
                return "text/html";
            case "js":
                return "application/javascript";
            case "css":
                return "text/css";
            case "json":
                return "application/json";
            case "png":
                return "image/png";
            case "jpg":
                return "image/jpg";
            case "ico":
                return "image/icon";
            case "gif":
                return "image/gif";
            default:
                return "unknown";
        }
    }

}
