package cn.edu.cuit.fatcat.container.session;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

public class SessionCollector implements HttpSessionContext {

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
