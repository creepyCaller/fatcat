package cn.edu.cuit.fatcat.container.session;

import cn.edu.cuit.fatcat.container.servlet.ServletCollector;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.*;

@Builder
@Slf4j
public class SessionContainer implements HttpSession {
    private String sessionId; // 会话ID
    private long creationTime; // 创建时间
    private long lastAccessTime; // 上次访问时间
    private int maxInactiveInterval; // 会话持续时间
    private Map<String, Object> attributes; // 属性

    public static SessionContainer create() {
        String uuid = UUID.randomUUID().toString();
        log.info("创建会话{}", uuid);
        return SessionContainer.builder()
                .sessionId(uuid)
                .creationTime(System.currentTimeMillis())
                .lastAccessTime(-1)
                .maxInactiveInterval(SessionConfig.INSTANCE.getSessionTimeout())
                .attributes(new HashMap<>())
                .build();
    }

    /**
     * 从会话上下文获取后调用一次设置最近访问时间
     */
    public void setAccessTime() {
        lastAccessTime = System.currentTimeMillis();
    }

    /**
     * Returns the time when this session was created, measured in milliseconds
     * since midnight January 1, 1970 GMT.
     *
     * @return a <code>long</code> specifying when this session was created,
     * expressed in milliseconds since 1/1/1970 GMT
     * @throws IllegalStateException if this method is called on an invalidated session
     */
    @Override
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * Returns a string containing the unique identifier assigned to this
     * session. The identifier is assigned by the servlet container and is
     * implementation dependent.
     *
     * @return a string specifying the identifier assigned to this session
     * @throws IllegalStateException if this method is called on an invalidated session
     */
    @Override
    public String getId() {
        return sessionId;
    }

    /**
     * Returns the last time the client sent a request associated with this
     * session, as the number of milliseconds since midnight January 1, 1970
     * GMT, and marked by the time the container received the request.
     * <p>
     * Actions that your application takes, such as getting or setting a value
     * associated with the session, do not affect the access time.
     *
     * @return a <code>long</code> representing the last time the client sent a
     * request associated with this session, expressed in milliseconds
     * since 1/1/1970 GMT
     * @throws IllegalStateException if this method is called on an invalidated session
     */
    @Override
    public long getLastAccessedTime() {
        return lastAccessTime;
    }

    /**
     * Returns the ServletContext to which this session belongs.
     *
     * @return The ServletContext object for the web application
     * @since 2.3
     */
    @Override
    public ServletContext getServletContext() {
        return ServletCollector.getInstance();
    }

    /**
     * Specifies the time, in seconds, between client requests before the
     * servlet container will invalidate this session. A zero or negative time
     * indicates that the session should never timeout.
     *
     * @param interval An integer specifying the number of seconds
     */
    @Override
    public void setMaxInactiveInterval(int interval) {
        maxInactiveInterval = interval;
    }

    /**
     * Returns the maximum time interval, in seconds, that the servlet container
     * will keep this session open between client accesses. After this interval,
     * the servlet container will invalidate the session. The maximum time
     * interval can be set with the <code>setMaxInactiveInterval</code> method.
     * A zero or negative time indicates that the session should never timeout.
     *
     * @return an integer specifying the number of seconds this session remains
     * open between client requests
     * @see #setMaxInactiveInterval
     */
    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    /**
     * Do not use.
     *
     * @return A dummy implementation of HttpSessionContext
     * @deprecated As of Version 2.1, this method is deprecated and has no
     * replacement. It will be removed in a future version of the
     * Java Servlet API.
     */
    @Override
    public HttpSessionContext getSessionContext() {
        return SessionCollector.getInstance();
    }

    /**
     * Returns the object bound with the specified name in this session, or
     * <code>null</code> if no object is bound under the name.
     *
     * @param name a string specifying the name of the object
     * @return the object with the specified name
     * @throws IllegalStateException if this method is called on an invalidated session
     */
    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    /**
     * @param name a string specifying the name of the object
     * @return the object with the specified name
     * @throws IllegalStateException if this method is called on an invalidated session
     * @deprecated As of Version 2.2, this method is replaced by
     * {@link #getAttribute}.
     */
    @Override
    public Object getValue(String name) {
        return attributes.get(name);
    }

    /**
     * Returns an <code>Enumeration</code> of <code>String</code> objects
     * containing the names of all the objects bound to this session.
     *
     * @return an <code>Enumeration</code> of <code>String</code> objects
     * specifying the names of all the objects bound to this session
     * @throws IllegalStateException if this method is called on an invalidated session
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        return new Vector<>(attributes.keySet()).elements();
    }

    /**
     * @return an array of <code>String</code> objects specifying the names of
     * all the objects bound to this session
     * @throws IllegalStateException if this method is called on an invalidated session
     * @deprecated As of Version 2.2, this method is replaced by
     * {@link #getAttributeNames}
     */
    @Override
    public String[] getValueNames() {
        return (String[]) attributes.keySet().toArray();
    }

    /**
     * Binds an object to this session, using the name specified. If an object
     * of the same name is already bound to the session, the object is replaced.
     * <p>
     * After this method executes, and if the new object implements
     * <code>HttpSessionBindingListener</code>, the container calls
     * <code>HttpSessionBindingListener.valueBound</code>. The container then
     * notifies any <code>HttpSessionAttributeListener</code>s in the web
     * application.
     * <p>
     * If an object was already bound to this session of this name that
     * implements <code>HttpSessionBindingListener</code>, its
     * <code>HttpSessionBindingListener.valueUnbound</code> method is called.
     * <p>
     * If the value passed in is null, this has the same effect as calling
     * <code>removeAttribute()</code>.
     *
     * @param name  the name to which the object is bound; cannot be null
     * @param value the object to be bound
     * @throws IllegalStateException if this method is called on an invalidated session
     */
    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    /**
     * @param name  the name to which the object is bound; cannot be null
     * @param value the object to be bound; cannot be null
     * @throws IllegalStateException if this method is called on an invalidated session
     * @deprecated As of Version 2.2, this method is replaced by
     * {@link #setAttribute}
     */
    @Override
    public void putValue(String name, Object value) {
        attributes.put(name, value);
    }

    /**
     * Removes the object bound with the specified name from this session. If
     * the session does not have an object bound with the specified name, this
     * method does nothing.
     * <p>
     * After this method executes, and if the object implements
     * <code>HttpSessionBindingListener</code>, the container calls
     * <code>HttpSessionBindingListener.valueUnbound</code>. The container then
     * notifies any <code>HttpSessionAttributeListener</code>s in the web
     * application.
     *
     * @param name the name of the object to remove from this session
     * @throws IllegalStateException if this method is called on an invalidated session
     */
    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    /**
     * @param name the name of the object to remove from this session
     * @throws IllegalStateException if this method is called on an invalidated session
     * @deprecated As of Version 2.2, this method is replaced by
     * {@link #removeAttribute}
     */
    @Override
    public void removeValue(String name) {
        attributes.remove(name);
    }

    /**
     * Invalidates this session then unbinds any objects bound to it.
     *
     * @throws IllegalStateException if this method is called on an already invalidated session
     */
    @Override
    public void invalidate() {
        log.info("会话“{}”超时，销毁", getId());
        SessionCollector.getInstance().removeSession(this);
    }

    /**
     * Returns <code>true</code> if the client does not yet know about the
     * session or if the client chooses not to join the session. For example, if
     * the server used only cookie-based sessions, and the client had disabled
     * the use of cookies, then a session would be new on each request.
     *
     * @return <code>true</code> if the server has created a session, but the
     * client has not yet joined
     * @throws IllegalStateException if this method is called on an already invalidated session
     */
    @Override
    public boolean isNew() {
        return lastAccessTime == -1;
    }

    /**
     * 自检
     * 返回true时保留
     * 返回false销毁
     * @return
     */
    public boolean selfCheck() {
        if (lastAccessTime < 0) {
            // 创建后未访问过，使用创建时间做决定
            if (System.currentTimeMillis() - creationTime > SessionConfig.INSTANCE.getSessionTimeoutMillis()) {
                return true;
            }
        } else {
            // 创建后访问过的话使用访问时间决定
            if (System.currentTimeMillis() - lastAccessTime > SessionConfig.INSTANCE.getSessionTimeoutMillis()) {
                return true;
            }
        }
        return false;
    }
}
