package cn.edu.cuit.linker.util;

import cn.edu.cuit.fatcat.setting.FatcatSetting;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class FileUtilTest {

    @Test
    public void test() {
        File favicon = new File(FatcatSetting.DEFAULT_FAVICON);
        System.out.println(favicon.getAbsolutePath());
        System.out.println(favicon.toPath());
    }

    @Test
    public void clearDirectory() {
        FileUtil.clearServerRoot();
    }
}