package cn.edu.cuit.linker;

import cn.edu.cuit.fatcat.setting.FatcatSetting;
import cn.edu.cuit.linker.io.Cache;
import cn.edu.cuit.linker.io.standard.StandardCache;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.ServerSocket;

/**
 * 使用HTTP协议，监听端口port，接收HTTP请求报文、返回HTTP响应报文
 *
 * @author  fpc
 * @date  2019/10/23
 * @since Fatcat 0.0.1
 */
@Slf4j
public class Server implements Runnable {
    public static final Cache cache = new StandardCache();

    /**
     * 用来承载一个Server实例的Runnable接口实现类
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(FatcatSetting.PORT)) {
            log.info("正在监听端口：{}", FatcatSetting.PORT);
            // TODO:使用线程池来做， getExecutor().execute(new SocketProcessor(wrapper));
            // TODO:在实现线程池之后，改用非阻断式
            while (true) {
                SocketWrapper socketWrapper = new SocketWrapper(serverSocket.accept());
                HttpHandler httpHandler = new HttpHandler(socketWrapper);
                Thread thread = new Thread(httpHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
