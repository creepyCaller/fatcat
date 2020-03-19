package cn.edu.cuit.fatcat.container;

public interface InstanceManager {

    public Object newInstanceByClazzName(String className) throws Throwable;

}
