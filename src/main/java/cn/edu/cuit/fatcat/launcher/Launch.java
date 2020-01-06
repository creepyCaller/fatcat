package cn.edu.cuit.fatcat.launcher;

import cn.edu.cuit.linker.Server;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import lombok.extern.slf4j.Slf4j;
import java.io.File;

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
            if ("-port".equals(args[0])) {
                FatcatSetting.port = Integer.parseInt(args[1]);
            }
        } catch (Throwable t) {
            log.warn("未设置服务器监听端口，使用默认值：" + FatcatSetting.port);
        }
        try {
            log.info("正在开始初始化...");
            Launch launch = new Launch();
            launch.init();
            log.info("服务器初始化成功!");
            log.info("正在启动服务器...");
            Server server = new Server();
            Thread serverThread = new Thread(server);
            serverThread.start();
            log.info("服务器启动成功!共耗时" + (System.currentTimeMillis() - startTime) + "毫秒");
        } catch (Exception e) {
            log.error("服务器启动失败!");
            e.printStackTrace();
        }
    }

    /**
     * 仅用于检查文件，可有可无
     */
    private void init() {
        File index = new File(FatcatSetting.WEB_APPLICATION + FatcatSetting.INDEX);
        if(!index.exists()) {
            log.error("找不到导航页!");
        }
    }

}
