package cn.edu.cuit.fatcat.container.servlet;

import cn.edu.cuit.fatcat.container.InstanceManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServletInstanceManager extends InstanceManager {
    private final ClassLoader servletClassLoader = new ServletClassLoader();

    public Servlet getServletInstance(ServletModel servletModel) throws Throwable {
        log.info("需创建实例: {}", servletModel.getServletClazz());
        // TODO: 在加载的时候就扫描load-on-startup属性加载，不然就不初始化
        Class<?> servletClazz = servletClassLoader.loadClass(servletModel.getServletClazz());
        Servlet servlet = (Servlet) servletClazz.newInstance();
        servletModel.setServletInstance(servlet);
        servletModel.setInit(true);
        return servletModel.getServletInstance();
    }

}
