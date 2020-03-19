package cn.edu.cuit.fatcat.container;

import cn.edu.cuit.fatcat.test.TestServlet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServletInstanceManager implements InstanceManager {

    @Override
    public Object newInstanceByClazzName(String className) throws Throwable {
        log.info("需创建实例: {}", className);
        return new TestServlet();
    }

}
