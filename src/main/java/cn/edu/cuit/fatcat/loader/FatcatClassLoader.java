package cn.edu.cuit.fatcat.loader;

import cn.edu.cuit.linker.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class FatcatClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] clazzBiStr = FileUtil.readClassRawFromClazzName(name);
        return defineClass(name, clazzBiStr, 0, clazzBiStr.length);
    }

}
