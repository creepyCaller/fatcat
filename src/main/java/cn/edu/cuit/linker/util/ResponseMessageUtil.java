package cn.edu.cuit.linker.util;

import cn.edu.cuit.fatcat.http.HttpContentType;

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
     * @param suffix 文件后缀名
     * @return 响应内容类型
     */
    public static String getContentType(String suffix) {
        switch (suffix) {
            case "css":
                return HttpContentType.TEXT_CSS;
            case "js":
                return HttpContentType.APPLICATION_JS;
            case "json":
                return HttpContentType.APPLICATION_JSON;
            case "png":
                return HttpContentType.IMAGE_PNG;
            case "jpg":
            case "jpeg":
                return HttpContentType.IMAGE_JPEG;
            case "ico":
                return HttpContentType.IMAGE_X_ICON;
            case "gif":
                return HttpContentType.IMAGE_GIF;
            case "svg":
                return HttpContentType.IMAGE_SVG;
            case "xml":
            case "txt":
                return HttpContentType.TEXT_XML;
            case "html":
            case "htm":
            default:
                return HttpContentType.TEXT_HTML;
        }
    }

}
