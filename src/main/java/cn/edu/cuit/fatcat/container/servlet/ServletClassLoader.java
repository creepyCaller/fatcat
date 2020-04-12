package cn.edu.cuit.fatcat.container.servlet;

import cn.edu.cuit.fatcat.loader.FatCatClassLoader;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;

@Slf4j
public class ServletClassLoader extends FatCatClassLoader {

    /**
     * 2020/3/23 2:41
     * 我在这里卡了半天了，一直出现NoClassDefFoundError
     * 可能是依赖的字节码没有被define，因为我吧Servlet.class放进WEB-INF并且define它，以后就变成java.lang.NoClassDefFoundError: java/lang/Object了
     * 正在寻找解决方法...
     * 2020/3/23 5:20
     * 好了，解决了，没什么技术上的难题
     * 主要原因是我把defineClass放在了loadClass里边，其实这倒是没什么问题，问题是我在那之前加了个if（查找name有没有被注册在Servlets里），这就导致其他需要加载的字节码没法define
     * 还有就是在类构造器中调用super(null)，导致无法将TestServlet上转为Servlet
     * 但是我值得反思，为什么我老不去翻阅官方的文档而是喜欢跑去翻博客翻SO，要是我早点去翻官方文档估计昨晚能出去散会步
     * https://docs.oracle.com/javase/7/docs/api/java/lang/ClassLoader.html
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> cls = super.findClass(name);
        if (cls == null) {
            throw new ClassNotFoundException(name);
        }
        if (Servlet.class.isAssignableFrom(cls)) {
            if (ServletCollector.getInstance().isRegistered(cls.getName())) {
                log.info("{} is registered", name);
                return cls;
            } else {
                log.info("{} is not registered!", name);
                throw new ClassNotFoundException(name);
            }
        }
        return cls;
    }

}

