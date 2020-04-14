package cn.edu.cuit.fatcat.container.servlet;

import lombok.extern.slf4j.Slf4j;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@Slf4j
public class ServletContainer implements ServletConfig {
    private String servletName; // Servlet的名字
    private String className; // 类名，连着包名的那种
    private Integer loadOnStartup = 0; // 是否在容器初始化时加载的flag
    private Map<String, String> initParam; // 参数
    private Vector<String> InitParameterNames;
    private volatile Servlet instance = null; // 实例，只允许单例
    private volatile boolean init = false; // 是否已经初始化

    public ServletContainer() {}

    public void setInitParam(String paramName, String paramValue) {
        if (initParam == null) {
            initParam = new HashMap<>();
        }
        initParam.put(paramName, paramValue);
    }

    public Servlet getInstance() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (!init) {
            synchronized (this) {
                if (!init) {
                    return ServletInstanceManager.INSTANCE.getServletInstance(this);
                }
            }
        }
        return instance;
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
        return ServletCollector.getInstance();
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
        if (initParam != null) {
            if (InitParameterNames == null) {
                InitParameterNames = new Vector<>(initParam.keySet());
            }
            return InitParameterNames.elements();
        }
        return null;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getLoadOnStartup() {
        return loadOnStartup;
    }

    public void setLoadOnStartup(Integer loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
    }

    public Map<String, String> getInitParam() {
        return initParam;
    }

    public void setInitParam(Map<String, String> initParam) {
        this.initParam = initParam;
    }

    public void setInitParameterNames(Vector<String> initParameterNames) {
        InitParameterNames = initParameterNames;
    }

    public void setInstance(Servlet instance) {
        this.instance = instance;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    @Override
    public String toString() {
        return "ServletContainer{" +
                "servletName='" + servletName + '\'' +
                ", className='" + className + '\'' +
                ", loadOnStartup=" + loadOnStartup +
                ", initParam=" + initParam +
                ", InitParameterNames=" + InitParameterNames +
                ", instance=" + instance +
                ", init=" + init +
                '}';
    }
}
