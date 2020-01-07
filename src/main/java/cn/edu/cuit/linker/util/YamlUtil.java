package cn.edu.cuit.linker.util;


import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * @author fpc
 */
public class YamlUtil {

    private final static DumperOptions OPTIONS;

    static {
        OPTIONS = new DumperOptions();
        OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    }

    public static LinkedHashMap getSettings() throws IOException {
        return (LinkedHashMap) (new Yaml(OPTIONS)).load(new FileReader(new File("Settings/Fatcat.yml")));
    }

}