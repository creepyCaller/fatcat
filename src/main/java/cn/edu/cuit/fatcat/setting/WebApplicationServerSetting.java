package cn.edu.cuit.fatcat.setting;

import cn.edu.cuit.fatcat.http.HttpContentLanguage;
import java.nio.charset.Charset;

/**
 * 服务器配置
 * INDEX: 网站导航页
 * ERROR_PAGE: 网站错误页
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
public class WebApplicationServerSetting {
    public static String ERROR_PAGE = "/error/error.html";
    public static String INDEX = "/index.html";
    public static String CHARSET_STRING = "UTF-8";
    public static Charset CHARSET = Charset.forName(WebApplicationServerSetting.CHARSET_STRING);
    public static String CONTENT_LANGUAGE = HttpContentLanguage.ZH_CN;
    static {

    }
}
