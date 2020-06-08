package cn.edu.cuit.fatcat.container.session;

import cn.edu.cuit.fatcat.container.Collector;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionCollector implements HttpSessionContext, Collector {
    private static SessionCollector instance;
    private Map<String, SessionContainer> sessions = new ConcurrentHashMap<>(); // TODO: 轮询销毁过期会话

    public static SessionCollector getInstance() {
        if (instance == null) {
            synchronized (SessionCollector.class) {
                if (instance == null) {
                    instance = new SessionCollector();
                }
            }
        }
        return instance;
    }

    /**
     * 在context创建一个session
     * @return
     */
    public SessionContainer createSessionContainer() {
        SessionContainer session = SessionContainer.create();
        sessions.put(session.getId(), session);
        return session;
    }

    /**
     * 通过JSESSIONID获取Session对象
     * 如果找不到的话就创建一个
     * 在获取之后都要修改JSESSIONID
     * @param jSessionId
     * @return
     */
    public SessionContainer getSessionContainer(String jSessionId) {
        SessionContainer session;
        if (jSessionId != null) {
            session = sessions.get(jSessionId);
            if (session != null) {
                session.setAccessTime();
            }
            return session;
        }
        return null;
    }

    /**
     * Do not use.
     *
     * @param sessionId Ignored
     * @return Always <code>null</code>
     * @deprecated As of Java Servlet API 2.1 with no replacement. This method
     * must return null and will be removed in a future version of
     * this API.
     */
    @Override
    public HttpSession getSession(String sessionId) {
        return null;
    }

    /**
     * Do not use.
     *
     * @return Always an empty Enumeration
     * @deprecated As of Java Servlet API 2.1 with no replacement. This method
     * must return an empty <code>Enumeration</code> and will be
     * removed in a future version of this API.
     */
    @Override
    public Enumeration<String> getIds() {
        return null;
    }
}
