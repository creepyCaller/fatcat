package cn.edu.cuit.fatcat.setting;

import cn.edu.cuit.fatcat.LifeCycle;
import cn.edu.cuit.fatcat.container.servlet.Mapping;
import cn.edu.cuit.fatcat.container.servlet.ServletModel;
import cn.edu.cuit.fatcat.container.servlet.Servlets;
import cn.edu.cuit.linker.util.FileUtil;
import cn.edu.cuit.linker.util.YamlUtil;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
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
public class FatcatSetting implements LifeCycle {

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
    private Document webxml;

    public void init() throws IOException {
        log.info("正在初始化服务器配置...");
        settings = YamlUtil.getSettings();
        FatcatSetting.ERROR_PAGES = new HashMap<>();
        FatcatSetting.CONTEXT_PARAM = new HashMap<>();
        FatcatSetting.WELCOME_LIST = new ArrayList<>();
    }

    private void initServletMapping() {
        NodeList servletMapping = webxml.getElementsByTagName("servlet-mapping");
        for (int i = 0; i < servletMapping.getLength() ; ++i) {
            Node mapping = servletMapping.item(i);
            String servletName = null;
            String urlPattern = null;
            for (Node iter = mapping.getFirstChild(); iter!= null; iter = iter.getNextSibling()) {
                if (iter.getNodeType() == Node.ELEMENT_NODE) {
                    switch (iter.getNodeName()) {
                        case "servlet-name":
                            servletName = iter.getFirstChild().getNodeValue();
                            break;
                        case "url-pattern":
                            urlPattern = iter.getFirstChild().getNodeValue();
                    }
                }
            }
            Mapping.setMapping(urlPattern, servletName);
        }
    }

    private void initServlets() {
        NodeList servlets = webxml.getElementsByTagName("servlet");
        for (int i = 0; i < servlets.getLength() ; ++i) {
            Node servletInfo = servlets.item(i);
            ServletModel servletModel = new ServletModel();
            for (Node node = servletInfo.getFirstChild(); node != null; node = node.getNextSibling()) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    switch (node.getNodeName()) {
                        case "servlet-name":
                            servletModel.setServletName(node.getFirstChild().getNodeValue());
                            break;
                        case "servlet-class":
                            servletModel.setServletClazz(node.getFirstChild().getNodeValue());
                            break;
                        case "load-on-startup":
                            servletModel.setLoadOnStartup(Integer.valueOf(node.getFirstChild().getNodeValue()));
                            break;
                        case "init-param":
                            String initParamName = null;
                            String initParamValue = null;
                            for (Node iter = node.getFirstChild(); iter != null; iter = iter.getNextSibling()) {
                                if (iter.getNodeType() == Node.ELEMENT_NODE) {
                                    switch (iter.getNodeName()) {
                                        case "param-name":
                                            initParamName = iter.getFirstChild().getNodeValue();
                                            break;
                                        case "param-value":
                                            initParamValue = iter.getFirstChild().getNodeValue();
                                            break;
                                    }
                                }
                            }
                            servletModel.setInitParam(initParamName, initParamValue);
                            break;
                    }
                }
            }
            Servlets.putServlet(servletModel.getServletName(), servletModel);
        }
    }

    private void initContextParams() {
        NodeList contextParams = webxml.getElementsByTagName("context-param");
        if (contextParams.getLength() > 0) {
            NodeList paramNames = webxml.getElementsByTagName("param-name");
            NodeList paramValues = webxml.getElementsByTagName("param-value");
            for (int i = 0; i < contextParams.getLength() ; ++i) {
                String paramName = paramNames.item(i).getFirstChild().getNodeValue();
                String paramValue = paramValues.item(i).getFirstChild().getNodeValue();
                FatcatSetting.CONTEXT_PARAM.put(paramName, paramValue);
            }
        }
        NodeList displayName = webxml.getElementsByTagName("display-name");
        if (displayName.getLength() > 0) {
            FatcatSetting.CONTEXT_PARAM.put("display-name", displayName.item(0).getFirstChild().getNodeValue());
        }
    }

    private void initErrorPages() {
        NodeList errorPages = webxml.getElementsByTagName("error-page");
        if (errorPages.getLength() > 0) {
            NodeList errorCodes = webxml.getElementsByTagName("error-code");
            NodeList pageDirs = webxml.getElementsByTagName("location");
            for (int i = 0; i < errorPages.getLength() ; ++i) {
                Integer errorCode = Integer.valueOf(errorCodes.item(i).getFirstChild().getNodeValue());
                String pageDir = pageDirs.item(i).getFirstChild().getNodeValue();
                FatcatSetting.ERROR_PAGES.put(errorCode, pageDir);
            }
        }
    }

    private void initWelcomeList() {
        NodeList welcomeList = webxml.getElementsByTagName("welcome-file-list");
        if (welcomeList.getLength() > 0) {
            NodeList welcomeFiles = webxml.getElementsByTagName("welcome-file");
            for (int i = 0; i < welcomeFiles.getLength() ; ++i) {
                String welcomePage = welcomeFiles.item(i).getFirstChild().getNodeValue();
                FatcatSetting.WELCOME_LIST.add(welcomePage);
            }
        }
    }

    @Override
    public void service() throws IOException {
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
        FatcatSetting.WAR = (String) settings.get("war");
        if (FatcatSetting.WAR == null) {
            log.warn("未设置待解压的WAR包");
        } else {
            FileUtil.clearServerRoot();
            if (!FatcatSetting.WAR.startsWith("/")) {
                FatcatSetting.WAR = "/" + FatcatSetting.WAR;
            }
            File file = new File("WAR" + FatcatSetting.WAR);
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
        webxml = FileUtil.getWebXML();
        if (webxml != null) {
            // TODO: 解耦到loader
            initContextParams();
            initWelcomeList();
            initErrorPages();
            initServletMapping();
            initServlets();
        }
    }

    @Override
    public void destroy() {
    }
}
