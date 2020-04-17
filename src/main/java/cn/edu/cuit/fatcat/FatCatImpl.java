package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.handler.Handler;
import cn.edu.cuit.fatcat.handler.RequestHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * FatCat容器入口方法
 *
 * @author  fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Slf4j
public class FatCatImpl implements FatCat {
    private Caller setting;

    @Override
    public void startUp() {
        log.info("FatCat/0.2");
        prepare();
        work();
    }

    @Override
    public void prepare() {
        try {
            setting = new Setting();
            setting.start();
        } catch (Throwable throwable) {
            log.info("启动失败");
            throwable.printStackTrace();
        }
    }

    @Override
    public void work() {
        log.info("启动Request Handler...");
        Handler requestHandler = RequestHandler.build(Setting.PORT);
        requestHandler.handle();
    }

    @Override
    public void done() {
    }
}
