package cn.edu.cuit.fatcat.container.servlet;

import cn.edu.cuit.fatcat.message.Request;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ServletMapping {
    private static ServletMapping instance;
    private final Map<String, String> mapper = new HashMap<>();

    private ServletMapping() {}

    public static ServletMapping getInstance() {
        if (instance == null) {
            instance = new ServletMapping();
        }
        return instance;
    }

    public void setMapping(String urlPattern, String servletName) {
        log.info("put({}, {})", urlPattern, servletName);
        mapper.put(urlPattern, servletName);
    }

    // TODO: 支持通配符*
    public String getServletName(String urlPattern) {
        return mapper.get(urlPattern);
    }

    public String getServletName(Request request) {
        return getServletName(request.getDirection());
    }
}
