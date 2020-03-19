package cn.edu.cuit.fatcat.container.servlet;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Servlets {

    private static final Map<String, ServletModel> servlets = new HashMap<>();

    public static void putServlet(String servletName, ServletModel servletModel) {
        log.info("put({}, {})", servletName, servletModel.toString());
        Servlets.servlets.put(servletName, servletModel);
    }

    public static ServletModel getServletModel(String servletName) {
        return Servlets.servlets.get(servletName);
    }

}
