package cn.edu.cuit.fatcat;

/**
 * 只是一个临时想法，用于回收已结束服务但是应该保留的实例
 */
public interface RecycleAble {

    /**
     * 调用此方法重置实例使实例回滚到刚实例化时候的状态
     */
    public void recycle();
}
