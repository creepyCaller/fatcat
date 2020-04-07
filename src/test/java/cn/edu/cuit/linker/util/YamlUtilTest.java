package cn.edu.cuit.linker.util;

import cn.edu.cuit.fatcat.util.YamlUtil;
import org.junit.Test;

import java.io.IOException;

public class YamlUtilTest {

    @Test
    public void getSettings() throws IOException {
        System.out.println(YamlUtil.getSettings().get("war"));
    }
}