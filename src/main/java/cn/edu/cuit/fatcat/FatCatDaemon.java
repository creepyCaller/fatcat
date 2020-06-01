package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.container.servlet.ServletCollector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * a daemon thread, for call System.gc() per 10min.
 * @author fpc
 */
public class FatCatDaemon implements Runnable {

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long callGCTime = System.currentTimeMillis();
        long needGCTime = 10 * 60 * 1000; // 每10分钟调用一次gc
        while (true) {
            if (System.currentTimeMillis() - callGCTime > needGCTime) {
                log.info("{}已经运行{}分钟了!还有{}MB可用内存, 调用System.gc()清理内存...", ServletCollector.getInstance().getServletContextName(), (System.currentTimeMillis() - startTime) / 1000 / 60, (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) / 1024 / 1024);
                callGCTime = System.currentTimeMillis();
                System.gc();
            } else {
                try {
                    Thread.sleep(needGCTime);
                } catch (InterruptedException ignored) { }
            }
        }
    }

    public static DaemonConfigurer getSetter() {
        return DaemonConfigurer.getInstance();
    }

    public static class DaemonConfigurer {
        private static DaemonConfigurer instance;
        private static FatCatDaemon fatCatDaemon;
        private static boolean started = false;

        private static DaemonConfigurer getInstance() {
            if (instance == null) {
                instance = new DaemonConfigurer();
                fatCatDaemon = new FatCatDaemon();
            }
            return instance;
        }

        public boolean isStarted() {
            return started;
        }

        public void startDaemonThread() {
            log.info("启动FatFatDaemon...");
            Thread thread = new Thread(fatCatDaemon);
            thread.setDaemon(true);
            thread.start();
            started = true;
        }
    }

}
