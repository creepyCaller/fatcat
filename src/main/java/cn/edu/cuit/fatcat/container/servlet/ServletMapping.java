package cn.edu.cuit.fatcat.container.servlet;

import cn.edu.cuit.fatcat.Dispatcher;
import cn.edu.cuit.fatcat.message.Request;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public enum ServletMapping {
    INSTANCE;

    private Map<String, String> mapper = new HashMap<>();
    private Map<String, String> mapperWithWildcard = new HashMap<>();

    public void setMapping(String urlPattern, String servletName) {
        log.info("setMapping({}, {})", urlPattern, servletName);
        mapper.put(urlPattern, servletName);
        if (urlPattern.contains("*")) {
            mapperWithWildcard.put(urlPattern, servletName);
        }
    }

    // TODO: 支持通配符*
    public String getServletName(String direction) {
        String servletName = mapper.get(direction);
        if (servletName != null) {
            // 如果能查到
            return servletName;
        }
        // 如果不能, 就遍历mapper寻找有没有匹配的
        if (mapperWithWildcard.containsKey("/*")) {
            // 先看看有没有全局匹配
            return mapperWithWildcard.get("/*");
        }
        for (Map.Entry entry : mapperWithWildcard.entrySet()) {
            // 遍历包含通配符的url看看有没有匹配的
            // 先分割
            String[] div = entry.getKey().toString().split("\\*");
            if (div.length == 1) {
                // 前缀匹配
                if (direction.startsWith(div[0])) {
                    return entry.getValue().toString();
                }
            }
            if (div.length > 1) {
                // 扩展名匹配
                if (div[1].startsWith(".")) {
                    if (direction.startsWith(div[0]) && direction.endsWith(div[1])) {
                        return entry.getValue().toString();
                    }
                }
            }
        }
        return null;
    }
}
