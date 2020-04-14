package cn.edu.cuit.fatcat;

/**
 * 功能型模块钩子，用于搭建FatCat框架
 */
public interface FunctionalModule {

    /**
     * 准备干活
     */
    public void prepare();

    /**
     * 干活
     */
    public void work() throws Throwable;

    /**
     * 收工
     */
    public void done();
}
