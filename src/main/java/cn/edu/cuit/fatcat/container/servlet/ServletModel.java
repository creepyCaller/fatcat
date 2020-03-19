package cn.edu.cuit.fatcat.container.servlet;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class ServletModel {
    private String servletName;
    private String servletClazz;
    private Integer loadOnStartup = 0;
    private Map<String, String> initParam;
    private Servlet servletInstance = null;

    public ServletModel() {}

    public void setInitParam(String paramName, String paramValue) {
        if (this.initParam == null) {
            this.initParam = new HashMap<>();
        }
        log.info("{} 放置初始变量 {} : {}", servletName, paramName, paramValue);
        this.initParam.put(paramName, paramValue);
    }

    public String getInitParam(String paramName) {
        if (this.initParam != null) {
            return this.initParam.get(paramName);
        }
        return null;
    }

    public Servlet getInstance() {
        return servletInstance;
    }

}
