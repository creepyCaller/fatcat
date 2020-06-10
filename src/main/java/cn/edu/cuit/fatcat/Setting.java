package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.container.session.SessionConfig;
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
 * connection_keep: 长连接持续时间（ms）
 * port：服务端口
 * auto_deploy: 是否自动部署
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
    public static String DISPLAY_NAME = "FatCat";
    public static String SERVER_ROOT = "WebApplication"; // 固定的服务器根目录名称
    public static String DEFAULT_FAVICON = "Resources/Default/Icon/favicon.ico";
    private static int DEFAULT_PORT = 8080; // 默认的服务端口号
    public static int PORT;
    public static int DEFAULT_CONNECTION_KEEP = 10000;
    public static int CONNECTION_KEEP;
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
        settings = YamlUtil.getSettings();
        Setting.ERROR_PAGES = new HashMap<>();
        Setting.CONTEXT_PARAM = new HashMap<>();
        Setting.WELCOME_LIST = new ArrayList<>();
    }

    @Override
    public void service() throws Throwable {
        Object port = settings.get("port");
        if (port == null) {
            Setting.PORT = Setting.DEFAULT_PORT;
        } else {
            if (port instanceof java.lang.Integer) {
                Setting.PORT = (Integer) port;
                if (Setting.PORT == 0) {
                    log.info("监听端口不能为0!将任意指派一个端口号");
                }
            } else {
                log.warn("监听端口设置错误, 使用默认值: {}", Setting.DEFAULT_PORT);
                Setting.PORT = Setting.DEFAULT_PORT;
            }
        }
        Object charset = settings.get("charset");
        if (charset == null) {
            log.warn("未设置编码, 使用默认值: {}", Setting.DEFAULT_CHARSET);
            Setting.CHARSET_STRING = Setting.DEFAULT_CHARSET;
        } else {
            if (charset instanceof java.lang.String) {
                Setting.CHARSET_STRING = (String) charset;
                log.info("使用编码: {}", Setting.CHARSET_STRING);
            } else {
                log.warn("编码设置错误, 使用默认值: {}", Setting.DEFAULT_CHARSET);
                Setting.CHARSET_STRING = Setting.DEFAULT_CHARSET;
            }
            // 通过编码实例化Charset对象
            try {
                Setting.CHARSET = Charset.forName(Setting.CHARSET_STRING);
            } catch (UnsupportedCharsetException ignore) {
                Setting.CHARSET = StandardCharsets.UTF_8;
            }
        }
        Object connection_keep = settings.get("connection_keep");
        if (connection_keep == null) {
            log.warn("未设置长连接持续时间, 使用默认值: {}ms", Setting.DEFAULT_CONNECTION_KEEP);
            Setting.CONNECTION_KEEP = Setting.DEFAULT_CONNECTION_KEEP;
        } else {
            if (connection_keep instanceof java.lang.Integer) {
                Setting.CONNECTION_KEEP = (Integer) connection_keep;
                log.info("长连接持续时间: {}ms", Setting.CONNECTION_KEEP);
            } else {
                log.warn("长连接持续时间设置错误, 使用默认值: {}ms", Setting.DEFAULT_CONNECTION_KEEP);
                Setting.CONNECTION_KEEP = Setting.DEFAULT_CONNECTION_KEEP;
            }
        }
        Object session_timeout = settings.get("session_timeout");
        if (session_timeout instanceof java.lang.Integer) {
            SessionConfig.INSTANCE.setSessionTimeout((Integer) session_timeout);
        }
        Setting.WAR = (String) settings.get("war");
        if (Setting.WAR != null) {
            Object auto_deploy = settings.get("auto_deploy");
            if (Boolean.TRUE.equals(auto_deploy)) {
                FileUtil.clearServerRoot();
                if (!Setting.WAR.startsWith("/")) {
                    Setting.WAR = "/" + Setting.WAR;
                }
                File file = new File("WAR" + Setting.WAR);
                if (file.exists()) {
                    log.info("部署WAR包: {}", Setting.WAR);
                    FileUtil.unpack(file);
                } else {
                    log.info("待部署WAR包不存在");
                }
            }
        }
        Caller webAppXMLLoader = new WebAppXMLLoader();
        webAppXMLLoader.start();
    }

    @Override
    public void destroy() {
    }
}
