package cn.edu.cuit.fatcat.container.servlet;

import lombok.extern.slf4j.Slf4j;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Servlets {
    private static final Map<String, ServletModel> servlets = new HashMap<>();

    public static void putServlet(String servletName, ServletModel servletModel) {
        log.info("put({}, {})", servletName, servletModel.toString());
        Servlets.servlets.put(servletName, servletModel);
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

}
