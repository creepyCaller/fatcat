package cn.edu.cuit.fatcat.handler;

import cn.edu.cuit.fatcat.Setting;
import cn.edu.cuit.fatcat.container.servlet.ServletCollector;
import cn.edu.cuit.fatcat.io.SocketWrapper;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使用HTTP协议，监听端口port，接收HTTP请求报文、返回HTTP响应报文
 *
 * @author  fpc
 * @date  2019/10/23
 * @since Fatcat 0.0.1
 */
@Slf4j
public class RequestHandler implements Handler, Runnable {
    private int port;

    private RequestHandler(int port) {
        this.port = port;
    }

    /**
     * 如果端口为0就随便指派一个端口号
     * @param port 端口号
     * @return 请求handler
     */
    public static RequestHandler build(int port) {
        while (port == 0) {
            port = (int) ((Math.random() * 100000) % 65535);
        }
        log.info("监听端口: {}", port);
        return new RequestHandler(port);
    }

    /**
     * pool存放在静态存储空间
     */
    private static ExecutorService pool;

    /**
     * 线程池getter
     * @return
     */
    private static ExecutorService getExecutor() {
        if (pool == null) {
            // 避免并发场景多次实例化Executor, 使用双重锁机制
            synchronized (RequestHandler.class) {
                if (pool == null) {
                    log.info("初始化线程池: 核心池{}, 最大池{}, 多余线程存活时间: {}ms",
                            Runtime.getRuntime().availableProcessors(),
                            Runtime.getRuntime().availableProcessors() * 2,
                            Setting.CONNECTION_KEEP);
                    pool = new ThreadPoolExecutor(
                            /*线程核心池大小=CPU核心数*/Runtime.getRuntime().availableProcessors(),
                            /*最大池大小*/Runtime.getRuntime().availableProcessors() * 2,
                            /*多余线程存活时间*/Setting.CONNECTION_KEEP,
                            /*存活时间单位*/TimeUnit.MILLISECONDS,
                            /*workQueue*/new LinkedBlockingQueue<>());
                }
            }
        }
        return pool;
    }

    @Override
    public void handle() {
        (new Thread(this)).start();
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long gcTime = System.currentTimeMillis();
        long needGCTime = 10 * 60 * 1000; // 每10分钟调用一次gc
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            // TODO: 非阻塞IO, 意思是换成ServerSocketChannel
            while (true) {
                SocketWrapper socketWrapper = SocketWrapper.wrapSocket(serverSocket.accept()); // 通过监听的端口接受套接字连接
                getExecutor().execute(() -> {
                    try {
                        HandlerSwitcher.INSTANCE.getHandler(socketWrapper).handle(); // 调用switcher, 返回Handler后调用handle
                    } catch (IOException e) {
                        log.error(e.toString());
                        e.printStackTrace();
                    }
                });
                if (System.currentTimeMillis() - gcTime > needGCTime) {
                    log.info("{}已经运行{}分钟了!还有{}MB可用内存, 调用System.gc()清理内存...", ServletCollector.getInstance().getServletContextName(), (System.currentTimeMillis() - startTime) / 1000 / 60, (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) / 1024 / 1024);
                    gcTime = System.currentTimeMillis();
                    System.gc();
                }
            }
        } catch (IOException e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }
}
