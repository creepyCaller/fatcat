package cn.edu.cuit.fatcat.loader;

import cn.edu.cuit.fatcat.Caller;
import cn.edu.cuit.fatcat.Setting;
import cn.edu.cuit.fatcat.container.servlet.Mapping;
import cn.edu.cuit.fatcat.container.servlet.ServletCollector;
import cn.edu.cuit.fatcat.container.servlet.ServletModel;
import cn.edu.cuit.fatcat.util.FileUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WebAppXMLLoader implements Caller {
    private Document webxml;

    @Override
    public void init() throws Throwable {
        webxml = FileUtil.getWebXML();
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
            ServletCollector.getInstance().putServlet(servletModel.getServletName(), servletModel);
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
                Setting.CONTEXT_PARAM.put(paramName, paramValue);
            }
        }
        NodeList displayName = webxml.getElementsByTagName("display-name");
        if (displayName.getLength() > 0) {
            Setting.CONTEXT_PARAM.put("display-name", displayName.item(0).getFirstChild().getNodeValue());
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
                Setting.ERROR_PAGES.put(errorCode, pageDir);
            }
        }
    }

    private void initWelcomeList() {
        NodeList welcomeList = webxml.getElementsByTagName("welcome-file-list");
        if (welcomeList.getLength() > 0) {
            NodeList welcomeFiles = webxml.getElementsByTagName("welcome-file");
            for (int i = 0; i < welcomeFiles.getLength() ; ++i) {
                String welcomePage = welcomeFiles.item(i).getFirstChild().getNodeValue();
                Setting.WELCOME_LIST.add(welcomePage);
            }
        }
    }

    @Override
    public void service() throws Throwable {
        if (webxml != null) {
            initContextParams();
            initWelcomeList();
            initErrorPages();
            initServletMapping();
            initServlets();
        }
    }

    @Override
    public void destroy() throws Throwable {
    }
}
