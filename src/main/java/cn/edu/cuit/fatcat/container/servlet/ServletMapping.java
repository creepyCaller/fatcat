package cn.edu.cuit.fatcat.container.servlet;

import cn.edu.cuit.fatcat.message.Request;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public enum ServletMapping {
    INSTANCE;

    private Map<String, String> mapper = new HashMap<>();

    public void setMapping(String urlPattern, String servletName) {
        log.info("setMapping({}, {})", urlPattern, servletName);
        mapper.put(urlPattern, servletName);
    }

    // TODO: 支持通配符*
    public String getServletName(String urlPattern) {
        return mapper.get(urlPattern);
    }

    public String getServletName(Request request) {
        return getServletName(request.getDirection());
    }

    public boolean containsServlet(String urlPattern) {
        return mapper.containsKey(urlPattern);
    }
}
