package cn.edu.cuit.fatcat.container.session;

import cn.edu.cuit.fatcat.FatCatDaemon;
import cn.edu.cuit.fatcat.container.servlet.ServletCollector;
import lombok.extern.slf4j.Slf4j;

/**
 * 会话的守护线程
 */
@Slf4j
public class SessionDaemon implements Runnable {

    @Override
    public void run() {
        long lastCleanTime = System.currentTimeMillis();
        long sleepTime = 60 * 1000; // 每1分钟进行轮询
        while (true) {
            if (System.currentTimeMillis() - lastCleanTime > SessionConfig.INSTANCE.getSessionTimeoutMillis()) {
                log.info("开始清理过期会话...");
                cleanSession();
                lastCleanTime = System.currentTimeMillis();
            } else {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ignored) { }
            }
        }
    }

    /**
     * 轮询清理过期会话
     */
    private void cleanSession() {
        SessionCollector.getInstance().cleanOldSession();
    }

    public static SessionDaemon.DaemonConfigurer getSetter() {
        return SessionDaemon.DaemonConfigurer.getInstance();
    }

    public static class DaemonConfigurer {
        private static SessionDaemon.DaemonConfigurer instance;
        private static SessionDaemon fatCatDaemon;
        private static boolean started = false;

        private static SessionDaemon.DaemonConfigurer getInstance() {
            if (instance == null) {
                instance = new SessionDaemon.DaemonConfigurer();
                fatCatDaemon = new SessionDaemon();
            }
            return instance;
        }

        public boolean isStarted() {
            return started;
        }

        public void startDaemonThread() {
            log.info("启动SessionDaemon...");
            Thread thread = new Thread(fatCatDaemon);
            thread.setDaemon(true);
            thread.start();
            started = true;
        }
    }
}
