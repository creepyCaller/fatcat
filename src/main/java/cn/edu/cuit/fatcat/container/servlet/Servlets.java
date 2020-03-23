package cn.edu.cuit.fatcat.container.servlet;

import lombok.extern.slf4j.Slf4j;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class Servlets {
    private static final Map<String, ServletModel> servlets = new HashMap<>();
    private static final Set<String> registered = new HashSet<>();

    public static void putServlet(String servletName, ServletModel servletModel) {
        log.info("put({}, {})", servletName, servletModel.toString());
        Servlets.servlets.put(servletName, servletModel);
        Servlets.registered.add(servletModel.getServletClazz());
    }

    public static ServletModel getServletModel(String servletName) throws ServletException {
        ServletModel servletModel = Servlets.servlets.get(servletName);
        if (servletModel == null) {
            throw new ServletException("not found: " + servletName);
        } else {
            return servletModel;
        }
    }

    public static boolean hasInstance(String servletName) {
        return Servlets.servlets.containsKey(servletName);
    }

    public static String getClazzName(String servletName) throws ServletException {
        return Servlets.getServletModel(servletName).getServletClazz();
    }

    /**
     * 判断是否已经在web.xml注册
     * @param servletClazz
     * @return
     */
    public static boolean isRegistered(String servletClazz) {
        return Servlets.registered.contains(servletClazz);
    }

}
