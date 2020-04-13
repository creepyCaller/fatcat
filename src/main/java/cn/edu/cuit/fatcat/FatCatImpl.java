package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.handler.Handler;
import cn.edu.cuit.fatcat.handler.SocketHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Fatcat容器入口方法
 *
 * @author  fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Slf4j
public class FatCatImpl implements FatCat {
    private long startTime;
    private Caller setting;

    @Override
    public void startUp() {
        prepare();
        work();
    }

    @Override
    public void prepare() {
        startTime = System.currentTimeMillis();
        setting = new Setting();
    }

    @Override
    public void work() {
        try {
            log.info("正在初始化服务器...");
            setting.start();
            log.info("服务器初始化成功");
            log.info("正在启动服务器...");
            Handler socketHandler = new SocketHandler();
            socketHandler.handle();
            log.info("服务器启动成功, 共耗时: {}ms", (System.currentTimeMillis() - startTime));
        } catch (Throwable e) {
            log.error("服务器启动失败");
            log.info(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void done() {
    }
}
