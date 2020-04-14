package cn.edu.cuit.fatcat.handler;

import cn.edu.cuit.fatcat.RunnableFunctionalModule;
import cn.edu.cuit.fatcat.Setting;
import cn.edu.cuit.fatcat.io.SocketWrapper;
import lombok.Data;
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
@Data
@Slf4j
public class RequestHandler implements Handler {

    @Override
    public void handle() {
        try(ServerSocket serverSocket = new ServerSocket(Setting.PORT)) {
            // TODO:使用线程池来做， getExecutor().execute(new ProtocolHandler(socketWrapper));
            // TODO:在实现线程池之后，改用非阻断式
            while (true) {
                SocketWrapper socketWrapper = SocketWrapper.newInstance(serverSocket.accept());
                SocketHandler.newThread(socketWrapper);
            }
        } catch (IOException e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }
}
