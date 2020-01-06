package cn.edu.cuit.fatcat.setting;

import cn.edu.cuit.fatcat.http.HttpContentLanguage;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务器配置
 * WEB_APPLICATION: 网站根目录
 * timeout: 连接超时自动断开（ms）
 * port：服务端口
 * INDEX: 网站导航页
 * ERROR_PAGE: 网站错误页
 * UNPACK_WAR: 待解压的WAR包名称,放置在WAR目录下,解压到FatcatSetting.WEB_APPLICATION目录下
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
public class FatcatSetting {
    public static String WEB_APPLICATION = "WebApplication";
    public long timeout = 20000;
    public static int port = 8080;
    public static Map<Integer, String> ERROR_PAGES; // 从web.xml读取
    public static String INDEX = "/index.html";
    public static String UNPACK_WAR = "/helloworld.war";
    public static String FAVICON = "/Resources/icon/favicon.ico";
    public static String CHARSET_STRING = "UTF-8";
    public static Charset CHARSET = Charset.forName(FatcatSetting.CHARSET_STRING);
    public static String CONTENT_LANGUAGE = HttpContentLanguage.ZH_CN;
    static {
        ERROR_PAGES = new HashMap<>();
        FatcatSetting.ERROR_PAGES.put(404, "/ErrorPages/404.html");
        FatcatSetting.ERROR_PAGES.put(500, "/ErrorPages/500.html");
    }
}
