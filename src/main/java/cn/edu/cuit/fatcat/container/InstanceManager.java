package cn.edu.cuit.fatcat.container;

/**
 * 　　当应用需要到某个类时，则会按照下面的顺序进行类加载：
 *
 * 　　1 使用bootstrap引导类加载器加载
 *
 * 　　2 使用system系统类加载器加载
 *
 * 　　3 使用应用类加载器在WEB-INF/classes中加载
 *
 * 　　4 使用应用类加载器在WEB-INF/lib中加载
 *
 * 　　5 使用common类加载器在CATALINA_HOME/lib中加载
 */
public class InstanceManager {

    public Object getInstance(String clazzName){
        return null;
    }

}
