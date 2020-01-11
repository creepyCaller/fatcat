package cn.edu.cuit.fatcat.launcher;

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
public class Launch {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("正在初始化服务器...");
            LifeCycle fatcatInit = new FatcatSetting();
            fatcatInit.service();
            log.info("服务器初始化成功");
            log.info("正在启动服务器...");
            (new Server()).run();
            log.info("服务器启动成功, 共耗时: {}ms", (System.currentTimeMillis() - startTime));
        } catch (Throwable e) {
            log.error("服务器启动失败");
            log.info(e.toString());
            e.printStackTrace();
        }
    }
}
