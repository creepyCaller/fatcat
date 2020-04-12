package cn.edu.cuit.fatcat.container.listener;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import java.util.EventListener;
import java.util.Map;
import java.util.Vector;

public class ListenerContainer {
    private String listenerName; // Servlet的名字
    private String className; // 类名，连着包名的那种
    private Map<String, String> initParam; // 参数
    private Vector<String> InitParameterNames;
    private volatile EventListener instance = null; // 实例，只允许单例
    private volatile boolean init = false; // 是否已经初始化


}
