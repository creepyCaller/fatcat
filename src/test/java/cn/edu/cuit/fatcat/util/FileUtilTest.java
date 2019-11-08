package cn.edu.cuit.fatcat.util;

import cn.edu.cuit.linker.util.FileUtil;
import org.junit.Test;

public class FileUtilTest {

    @Test
    public void testGetFileSuffix() {
        String subffix = FileUtil.getFileSuffix("bootstrap.min.css");
        System.out.println(subffix);
    }
}