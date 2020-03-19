package cn.edu.cuit.fatcat;

public interface LifeCycle {
    public void init() throws Throwable;
    public void service() throws Throwable;
    public void destroy() throws Throwable;
    public default void start() throws Throwable {
        this.init();
        this.service();
        this.destroy();
    }
}
