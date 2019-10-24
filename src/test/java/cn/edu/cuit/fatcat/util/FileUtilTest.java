package cn.edu.cuit.fatcat.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileUtilTest {

    @Test
    public void getFileSuffix() {
        String subffix = FileUtil.getFileSuffix("bootstrap.min.css");
        System.out.println(subffix);
    }
}