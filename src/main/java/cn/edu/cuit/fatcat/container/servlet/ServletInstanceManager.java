package cn.edu.cuit.fatcat.container.servlet;

import cn.edu.cuit.fatcat.container.InstanceManager;
import cn.edu.cuit.fatcat.loader.ServletClassLoader;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.Servlet;

@Slf4j
public class ServletInstanceManager extends InstanceManager {
    private final ClassLoader servletClassLoader = new ServletClassLoader();
    private static ServletInstanceManager instance;

    private ServletInstanceManager() {}

    public static ServletInstanceManager getInstance() {
        if (instance == null) {
            instance = new ServletInstanceManager();
        }
        return instance;
    }

    public Servlet getServletInstance(ServletModel servletModel) throws Throwable {
        log.info("需创建实例: {}", servletModel.getServletClazz());
        // TODO: 在扫描的时候看load-on-startup属性初不初始化
        Class<?> servletClazz = servletClassLoader.loadClass(servletModel.getServletClazz());
        Servlet servlet = (Servlet) servletClazz.newInstance();
        servletModel.setServletInstance(servlet);
        servletModel.setInit(true);
        return servletModel.getServletInstance();
    }

}
