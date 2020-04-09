package cn.edu.cuit.fatcat.container.servlet;

import cn.edu.cuit.fatcat.container.InstanceManager;
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

    public Servlet getServletInstance(ServletModel servletModel) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        long start;
        log.info("需创建实例: {}", servletModel.getClassName());
        start = System.currentTimeMillis();
        // TODO: 在扫描的时候看load-on-startup属性初不初始化
        Class<?> servletClazz = servletClassLoader.loadClass(servletModel.getClassName());
        Servlet servlet = (Servlet) servletClazz.newInstance();
        log.info("成功创建实例: {}, 耗时: {}ms", servletModel.getClassName(), (System.currentTimeMillis() - start));
        servletModel.setServletInstance(servlet);
        servletModel.setInit(true);
        return servlet;
    }

}
