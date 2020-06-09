package cn.edu.cuit.fatcat.container.servlet;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Servlet映射
 * 该类是一个枚举单例类, 用来存储和获取Url -> ServletName 的映射关系
 *
 * @author fpc
 * @since FatCat/0.2
 */
@Slf4j
public enum ServletMapping {
    INSTANCE;

    /**
     * 存储 URL -> ServletName 的映射关系的哈希表
     */
    private Map<String, String> mapper = new HashMap<>();

    /**
     * 存储含有 '*' 的Servlet映射路径
     */
    private Map<String, String> mapperWithWildcard = new HashMap<>();

    /**
     * 存储通配符使用*分割后的数组，这样不用每次分割以提高效率
     */
    private Map<String, String[]> divs = new HashMap<>();


    /**
     * 设置映射关系, 仅在启动时调用
     *
     * @param urlPattern 统一资源定位符
     * @param servletName servlet的注册名
     */
    public void setMapping(String urlPattern, String servletName) {
        log.info("设置URL映射: {} -> {}", urlPattern, servletName);
        mapper.put(urlPattern, servletName);
        if (urlPattern.contains("*")) {
            // 如果预设的urlPattern包含通配符, 就放到mapperWithWildcard对象中
            mapperWithWildcard.put(urlPattern, servletName);
            // 存储使用*号分割后的字符串
            divs.put(urlPattern, urlPattern.split("\\*"));
        }
    }

    /**
     * 使用请求报文的direction获取Servlet注册名
     *
     * @param direction 请求路径
     * @return Servlet注册名
     */
    public String getServletName(String direction) {
        String servletName = mapper.get(direction);
        if (servletName != null) {
            // 使用dir直接查到，就返回对应对象
            return servletName;
        }
        // 如果不能, 就遍历mapper寻找有没有匹配的
        // 先看看有没有全局匹配
        if (mapperWithWildcard.containsKey("/*")) {
            // 如果包含 '/*'通配符就返回它
            return mapperWithWildcard.get("/*");
        }
        // 遍历包含通配符的url看看有没有匹配的
        for (Map.Entry<String, String> entry : mapperWithWildcard.entrySet()) {
            // 获取预先存储好的分割后的数组
            String[] div = divs.get(entry.getKey());
            if (div.length == 1) {
                // 前缀匹配
                // 比如: /hello/aaa*, 就匹配前缀字符串/hello/aaa
                if (direction.startsWith(div[0])) {
                    return entry.getValue();
                }
            }
            if (div.length > 1) {
                // 扩展名匹配
                // 比如: /hello/aaa*.do, 就匹配*的两边
                if (div[1].startsWith(".")) {
                    if (direction.startsWith(div[0]) && direction.endsWith(div[1])) {
                        return entry.getValue();
                    }
                }
            }
        }
        // 如果都没, 就看看有没有默认Servlet
        if (mapper.containsKey("/")) {
            return mapper.get("/");
        }
        // 如果都没有, 就返回null
        return null;
    }
}
