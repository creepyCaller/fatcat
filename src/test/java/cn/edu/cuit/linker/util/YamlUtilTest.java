package cn.edu.cuit.linker.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class YamlUtilTest {

    @Test
    public void getFromYml() throws IOException {
        System.out.println(YamlUtil.getSettings().get("war"));
    }
}