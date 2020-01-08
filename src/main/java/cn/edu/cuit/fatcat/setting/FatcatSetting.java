package cn.edu.cuit.fatcat.setting;

import cn.edu.cuit.linker.util.FileUtil;
import cn.edu.cuit.linker.util.YamlUtil;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
@Slf4j
public class FatcatSetting {
    public static String SERVER_ROOT = "WebApplication"; // 固定的服务器根目录名称
    private static Integer DEFAULT_PORT = 8080; // 默认的服务端口号
    public static Integer PORT;
    public static String DEFAULT_WELCOME = "/index.html"; // 从web.xml读取,默认为"/index.html"
    public static String WAR; // 待解压的WAR包名,如果不存在就跳过解压
    public static String CHARSET_STRING; // 编码
    private static String DEFAULT_CHARSET = "UTF-8";
    public static Charset CHARSET;
    public static Map<Integer, String> ERROR_PAGES; // 从web.xml读取, 错误码对应的错误页
    public static void init() throws IOException {
        log.info("正在初始化服务器配置...");
        LinkedHashMap settings = YamlUtil.getSettings();
        if (settings.get("port") == null) {
            log.warn("未设置服务端口, 使用默认值: {}", FatcatSetting.DEFAULT_PORT);
            FatcatSetting.PORT = FatcatSetting.DEFAULT_PORT;
        } else {
            if (settings.get("port") instanceof java.lang.Integer) {
                FatcatSetting.PORT = (Integer) settings.get("port");
                log.info("服务端口: {}", FatcatSetting.PORT);
            } else {
                log.warn("服务端口设置错误, 使用默认值: {}", FatcatSetting.DEFAULT_PORT);
                FatcatSetting.PORT = FatcatSetting.DEFAULT_PORT;
            }
        }
        if (settings.get("charset") == null) {
            log.warn("未设置编码, 使用默认值: {}", FatcatSetting.DEFAULT_CHARSET);
            FatcatSetting.CHARSET_STRING = FatcatSetting.DEFAULT_CHARSET;
        } else {
            if (settings.get("charset") instanceof java.lang.String) {
                FatcatSetting.CHARSET_STRING = (String) settings.get("charset");
                log.info("使用编码: {}", FatcatSetting.CHARSET_STRING);
            } else {
                log.warn("编码设置错误, 使用默认值: {}", FatcatSetting.DEFAULT_CHARSET);
                FatcatSetting.CHARSET_STRING = FatcatSetting.DEFAULT_CHARSET;
            }
        }
        FatcatSetting.CHARSET = Charset.forName(FatcatSetting.CHARSET_STRING);
        if (settings.get("war") == null) {
            log.warn("未设置待解压的WAR包");
        } else {
            String war = (String) settings.get("war");
            if (!war.startsWith("/")) {
                war = "/" + war;
            }
            File file = new File("WAR" + war);
            if (file.exists()) {
                log.info("待解压WAR包位于: {}", file.getAbsolutePath());
                FileUtil.clearServerRoot();
                log.info("开始解压WAR包...");
                long start = System.currentTimeMillis();
                FileUtil.unpack(file);
                log.info("解压完成, 共耗时: {}ms", System.currentTimeMillis() - start);
            } else {
                log.info("待解压WAR包不存在");
            }
        }
        ERROR_PAGES = new HashMap<>();
        FatcatSetting.ERROR_PAGES.put(404, "/ErrorPages/404.html");
        FatcatSetting.ERROR_PAGES.put(500, "/ErrorPages/500.html");
    }
}
