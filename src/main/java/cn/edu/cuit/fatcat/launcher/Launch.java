package cn.edu.cuit.fatcat.launcher;

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
public class Launch {

    public static void main(String[] args) {
        // 如果什么都没设置，则默认为"-port 80"
        long startTime = System.currentTimeMillis();

        try {
            log.info("开始初始化...");
            FatcatSetting.init();
            log.info("服务器初始化成功!");
            log.info("正在启动服务器...");
            Server server = new Server();
            Thread serverThread = new Thread(server);
            serverThread.start();
            log.info("服务器启动成功!共耗时: {}ms", (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            log.error("服务器启动失败!");
            log.info(e.toString());
        }
    }
}
