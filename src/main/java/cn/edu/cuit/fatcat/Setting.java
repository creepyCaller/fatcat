package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.loader.WebAppXMLLoader;
import cn.edu.cuit.fatcat.util.FileUtil;
import cn.edu.cuit.fatcat.util.YamlUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;

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
@Slf4j
public class Setting implements Caller {
    // TODO: 使用getter和setter包装
    public static String HOST = "localhost";
    public static String SERVER_ROOT = "WebApplication"; // 固定的服务器根目录名称
    public static String DEFAULT_FAVICON = "Resources/Default/Icon/favicon.ico";
    private static Integer DEFAULT_PORT = 8080; // 默认的服务端口号
    public static Integer PORT;
    public static String DEFAULT_WELCOME = "/index.html"; // 从web.xml读取,默认为"/index.html"
    public static List<String> WELCOME_LIST;
    public static String WAR; // 待解压的WAR包名,如果不存在就跳过解压
    public static String CHARSET_STRING; // 编码
    private static String DEFAULT_CHARSET = "UTF-8";
    public static Charset CHARSET;
    public static Map<Integer, String> ERROR_PAGES; // 从web.xml读取, 错误码对应的错误页
    public static Map<String, String> CONTEXT_PARAM; // 上下文参数
    private LinkedHashMap settings;

    public void init() throws IOException {
        log.info("正在初始化服务器配置...");
        settings = YamlUtil.getSettings();
        Setting.ERROR_PAGES = new HashMap<>();
        Setting.CONTEXT_PARAM = new HashMap<>();
        Setting.WELCOME_LIST = new ArrayList<>();
    }

    @Override
    public void service() throws Throwable {
        if (settings.get("port") == null) {
            log.warn("未设置服务端口, 使用默认值: {}", Setting.DEFAULT_PORT);
            Setting.PORT = Setting.DEFAULT_PORT;
        } else {
            if (settings.get("port") instanceof java.lang.Integer) {
                Setting.PORT = (Integer) settings.get("port");
                log.info("服务端口: {}", Setting.PORT);
            } else {
                log.warn("服务端口设置错误, 使用默认值: {}", Setting.DEFAULT_PORT);
                Setting.PORT = Setting.DEFAULT_PORT;
            }
        }
        if (settings.get("charset") == null) {
            log.warn("未设置编码, 使用默认值: {}", Setting.DEFAULT_CHARSET);
            Setting.CHARSET_STRING = Setting.DEFAULT_CHARSET;
        } else {
            if (settings.get("charset") instanceof java.lang.String) {
                Setting.CHARSET_STRING = (String) settings.get("charset");
                log.info("使用编码: {}", Setting.CHARSET_STRING);
            } else {
                log.warn("编码设置错误, 使用默认值: {}", Setting.DEFAULT_CHARSET);
                Setting.CHARSET_STRING = Setting.DEFAULT_CHARSET;
            }
        }
        try {
            Setting.CHARSET = Charset.forName(Setting.CHARSET_STRING);
        } catch (UnsupportedCharsetException ignore) {
            Setting.CHARSET = StandardCharsets.UTF_8;
        }
        Setting.WAR = (String) settings.get("war");
        if (Setting.WAR == null) {
            log.warn("未设置待解压的WAR包");
        } else {
            FileUtil.clearServerRoot();
            if (!Setting.WAR.startsWith("/")) {
                Setting.WAR = "/" + Setting.WAR;
            }
            File file = new File("WAR" + Setting.WAR);
            if (file.exists()) {
                log.info("待解压WAR包位于: {}", file.getAbsolutePath());
                log.info("开始解压WAR包...");
                long start = System.currentTimeMillis();
                FileUtil.unpack(file);
                log.info("解压完成, 共耗时: {}ms", System.currentTimeMillis() - start);
            } else {
                log.info("待解压WAR包不存在");
            }
        }
        Caller webAppXMLLoader = new WebAppXMLLoader();
        webAppXMLLoader.start();
    }

    @Override
    public void destroy() {
    }
}
