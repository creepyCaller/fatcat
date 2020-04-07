package cn.edu.cuit.fatcat.container.servlet;

import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Mapping {
    private static final Map<String, String> mapper = new HashMap<>();

    public static void setMapping(String urlPattern, String servletName) {
        log.info("put({}, {})", urlPattern, servletName);
        Mapping.mapper.put(urlPattern, servletName);
    }

    // TODO: 支持通配符*
    public static String getServletName(String urlPattern) {
        return Mapping.mapper.get(urlPattern);
    }

}
