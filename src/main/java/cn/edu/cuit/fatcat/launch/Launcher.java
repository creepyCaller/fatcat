package cn.edu.cuit.fatcat.launch;

import cn.edu.cuit.fatcat.LifeCycle;
import cn.edu.cuit.linker.Server;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import lombok.extern.slf4j.Slf4j;

/**
 * Fatcat容器入口方法
 *
 * @author  fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Slf4j
public class Launcher {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("正在初始化服务器...");
            LifeCycle fatcatInit = new FatcatSetting();
            fatcatInit.start();
            log.info("服务器初始化成功");
            log.info("正在启动服务器...");
            Server server = new Server();
            Thread serverThread = new Thread(server);
            serverThread.start();
            log.info("服务器启动成功, 共耗时: {}ms", (System.currentTimeMillis() - startTime));
        } catch (Throwable e) {
            log.error("服务器启动失败");
            log.info(e.toString());
            e.printStackTrace();
        }
    }
}
