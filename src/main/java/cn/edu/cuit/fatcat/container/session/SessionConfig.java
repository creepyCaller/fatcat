package cn.edu.cuit.fatcat.container.session;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum SessionConfig {
    INSTANCE;

    public static final String JSESSIONID = "JSESSIONID";

    private static final int DEFAULT_SESSION_TIMEOUT = 10;

    private int SESSION_TIMEOUT = -1;

    private long SESSION_TIMEOUT_MILLS = -1;

    public void setSessionTimeout(int sessionTimeout) {
        log.info("设置会话超时时间{}分钟", sessionTimeout);
        SESSION_TIMEOUT = sessionTimeout;
    }

    public int getSessionTimeout() {
        if (SESSION_TIMEOUT < 0) {
            log.info("未设置会话超时时间，使用默认值:{}分钟", DEFAULT_SESSION_TIMEOUT);
            SESSION_TIMEOUT = DEFAULT_SESSION_TIMEOUT;
        }
        return SESSION_TIMEOUT;
    }

    public long getSessionTimeoutMillis() {
        if (SESSION_TIMEOUT_MILLS < 0) {
            SESSION_TIMEOUT_MILLS = (long) getSessionTimeout() * 1000L;
        }
        return SESSION_TIMEOUT_MILLS;
    }
}
