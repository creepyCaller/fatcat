package cn.edu.cuit.fatcat.container.session;

public enum SessionConfig {
    INSTANCE;

    public static final String JSESSIONID = "JSESSIONID";

    private static final int DEFAULT_SESSION_TIMEOUT = 10;

    private int SESSION_TIMEOUT = -1;

    public void setSessionTimeout(int sessionTimeout) {
        SESSION_TIMEOUT = sessionTimeout;
    }

    public int getSessionTimeout() {
        if (SESSION_TIMEOUT < 0) {
            SESSION_TIMEOUT = DEFAULT_SESSION_TIMEOUT;
        }
        return SESSION_TIMEOUT;
    }
}
