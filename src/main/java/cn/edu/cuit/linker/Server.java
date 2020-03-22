package cn.edu.cuit.linker;

import cn.edu.cuit.fatcat.container.servlet.ServletInstanceManager;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
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
    public static final ServletInstanceManager servletInstanceManager = new ServletInstanceManager();


    /**
     * 用来承载一个Server实例的Runnable接口实现类
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(FatcatSetting.PORT)) {
            log.info("正在监听端口：{}", FatcatSetting.PORT);
            // TODO:使用线程池来做， getExecutor().execute(new HttpHandler(socketWrapper));
            // TODO:在实现线程池之后，改用非阻断式
            while (true) {
                SocketWrapper socketWrapper = new SocketWrapper(serverSocket.accept());
                HttpHandler httpHandler = new HttpHandler(socketWrapper);
                (new Thread(httpHandler)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
