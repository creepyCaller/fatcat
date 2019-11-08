package cn.edu.cuit.fatcat.util;

import cn.edu.cuit.linker.util.ArrayUtil;
import org.junit.Test;

import java.util.Arrays;

public class ArrayUtilTest {

    @Test
    public void testMerge() {
        byte[] a = new byte[]{0,0,0,0,0};
        byte[] b = new byte[]{1,1,1,1,1};
        byte[] c = ArrayUtil.ByteArrayMerge(a, b);
        System.out.println(Arrays.toString(c));
    }
}