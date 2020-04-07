package cn.edu.cuit.fatcat;

public interface Caller extends LifeCycle {

    public default void start() throws Throwable {
        this.init();
        this.service();
        this.destroy();
    }
}
