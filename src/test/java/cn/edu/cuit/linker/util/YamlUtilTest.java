package cn.edu.cuit.linker.util;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;

import static org.junit.Assert.*;

public class YamlUtilTest {

    @Test
    public void getSettings() throws IOException {
        System.out.println(YamlUtil.getSettings().get("war"));
    }
}