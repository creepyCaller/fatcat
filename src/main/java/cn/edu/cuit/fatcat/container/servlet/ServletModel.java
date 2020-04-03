package cn.edu.cuit.fatcat.container.servlet;

import cn.edu.cuit.linker.Server;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@Data
@Slf4j
public class ServletModel implements ServletConfig {
    private String servletName; // Servlet的名字
    private String servletClazz; // 类名，连着包名的那种
    private Integer loadOnStartup = -1; // 是否在容器初始化时加载的flag
    private Map<String, String> initParam; // 参数
    private volatile Servlet servletInstance = null; // 实例，只允许单例
    private volatile boolean init = false; // 是否已经初始化

    public ServletModel() {}

    public void setInitParam(String paramName, String paramValue) {
        if (this.initParam == null) {
            this.initParam = new HashMap<>();
        }
        this.initParam.put(paramName, paramValue);
    }

    public Servlet getInstance() throws Throwable {
        if (init) {
            return servletInstance;
        } else {
            return Server.servletInstanceManager.getServletInstance(this);
        }
    }

    /**
     * Returns the name of this servlet instance.
     * The name may be provided via server administration, assigned in the
     * web application deployment descriptor, or for an unregistered (and thus
     * unnamed) servlet instance it will be the servlet's class name.
     *
     * @return	the name of the servlet instance
     */
    public String getServletName() {
        return servletName;
    }

    /**
     * Returns a reference to the {@link ServletContext} in which the caller
     * is executing.
     *
     * @return a {@link ServletContext} object, used
     * by the caller to interact with its servlet container
     * @see ServletContext
     */
    @Override
    public ServletContext getServletContext() {
        return Server.servlets;
    }

    /**
     * Gets the value of the initialization parameter with the given name.
     *
     * @param name the name of the initialization parameter whose value to
     *             get
     * @return a <code>String</code> containing the value
     * of the initialization parameter, or <code>null</code> if
     * the initialization parameter does not exist
     */
    @Override
    public String getInitParameter(String name) {
        if (initParam != null) {
            return initParam.get(name);
        }
        return null;
    }

    /**
     * Returns the names of the servlet's initialization parameters
     * as an <code>Enumeration</code> of <code>String</code> objects,
     * or an empty <code>Enumeration</code> if the servlet has
     * no initialization parameters.
     *
     * @return an <code>Enumeration</code> of <code>String</code>
     * objects containing the names of the servlet's
     * initialization parameters
     */
    @Override
    public Enumeration<String> getInitParameterNames() {
        Vector<String> paramNames = new Vector<>();
        initParam.forEach((key, value) -> paramNames.add(key));
        return paramNames.elements();
    }
}
