package cn.edu.cuit.fatcat.container.servlet;

import cn.edu.cuit.fatcat.loader.ServletClassLoader;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.Servlet;

@Slf4j
public enum ServletInstanceManager {
    INSTANCE;

    private final ClassLoader servletClassLoader = new ServletClassLoader();

    public Servlet getServletInstance(ServletContainer servletContainer) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        long start;
        log.info("需创建Servlet实例: {}", servletContainer.getClassName());
        start = System.currentTimeMillis();
        Class<?> servletClazz = servletClassLoader.loadClass(servletContainer.getClassName());
        Servlet servlet = (Servlet) servletClazz.newInstance();
        log.info("成功创建Servlet实例: {}, 耗时: {}ms", servletContainer.getClassName(), (System.currentTimeMillis() - start));
        servletContainer.setInstance(servlet);
        servletContainer.setInit(true);
        return servlet;
    }
}
