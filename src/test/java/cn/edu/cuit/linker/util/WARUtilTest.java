package cn.edu.cuit.linker.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class WARUtilTest {

    @Test
    public void unpack() throws IOException {
        File file = new File("WAR", (String) YamlUtil.getSettings().get("war"));
        WARUtil.unpack(file);
    }
}