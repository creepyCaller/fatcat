package cn.edu.cuit.fatcat.util;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ArrayUtilTest {

    @Test
    public void testMerge() {
        byte[] a = new byte[]{0,0,0,0,0};
        byte[] b = new byte[]{1,1,1,1,1};
        byte[] c = ArrayUtil.BiyeArrayMerge(a, b);
        System.out.println(Arrays.toString(c));
    }
}