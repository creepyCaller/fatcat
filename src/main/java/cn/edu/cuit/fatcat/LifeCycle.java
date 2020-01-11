package cn.edu.cuit.fatcat;

public interface LifeCycle {
    public void init() throws Throwable;
    public void start() throws Throwable;
    public void stop() throws Throwable;
    public void destroy() throws Throwable;
    public default void service() throws Throwable {
        this.init();
        this.start();
        this.stop();
        this.destroy();
    }
}
