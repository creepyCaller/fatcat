package cn.edu.cuit.fatcat.handler;

import cn.edu.cuit.fatcat.RunnableFunctionalModule;


public interface ProtocolHandler extends Handler, RunnableFunctionalModule {

    public default void handle() {
        (new Thread(this)).start();
    }

    public boolean isClose();

    public default void run() {
        prepare();
        try {
            do {
                // TODO: 限制同一个Client建立socket的数量
                    work();
            } while (!isClose());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        done();
    }
}
