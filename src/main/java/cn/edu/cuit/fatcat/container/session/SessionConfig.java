package cn.edu.cuit.fatcat.container.session;

public enum SessionConfig {
    INSTANCE;

    private static final int DEFAULT_SESSION_TIMEOUT = 10;

    private int SESSION_TIMEOUT;

    private void setSessionTimeout(int sessionTimeout) {
        this.SESSION_TIMEOUT = sessionTimeout;
    }
}
