package cn.edu.cuit.linker;

import cn.edu.cuit.fatcat.GenesisServlet;
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

    /**
     * 用来承载一个Server实例的Runnable接口实现类
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(FatcatSetting.port); // 换用SocketWrapper
            log.info("正在监听端口：" + FatcatSetting.port);
            // 在实现线程池之后，改用非阻断式
            // 使用线程池来做， getExecutor().execute(new SocketProcessor(wrapper));
            while (true) {
                SocketHandler socketHandler = new SocketHandler(serverSocket.accept());
                (new Thread((new GenesisServlet(socketHandler)))).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
