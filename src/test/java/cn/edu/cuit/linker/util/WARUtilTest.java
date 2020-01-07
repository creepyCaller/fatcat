package cn.edu.cuit.linker.util;

import cn.edu.cuit.fatcat.setting.FatcatSetting;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.*;

@Slf4j
public class WARUtilTest {

    @Test
    public void unpack() throws IOException {
        File serverRoot = new File(FatcatSetting.SERVER_ROOT);
        log.info("正在清空目录...");
        this.directoryHandler(serverRoot);
        log.info("清空目录成功");
        File file = new File("WAR", (String) YamlUtil.getSettings().get("war"));
        WARUtil.unpack(file);
    }

    private void directoryHandler(File dir) {
        File[] files = Objects.requireNonNull(dir.listFiles());
        for (File iter : files) {
            if (iter.isFile()) {
                this.fileHandler(iter);
            } else if (iter.isDirectory()) {
                this.directoryHandler(iter);
                if (iter.delete()) {
                    log.info("删除文件夹: {}", iter.getName());
                }
            }
        }
    }

    private void fileHandler(File file) {
        if (file.delete()) {
            log.info("删除文件: {}", file.getName());
        }
    }
}