package cn.edu.cuit.fatcat.util;

/**
 * 数组工具类
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
public class ArrayUtil {

    /**
     * 合并两个byte数组
     *
     * @param a 0 ~ a
     * @param b a ~ a+b
     * @return 合并后的数组,[a, b]
     */
    public static byte[] BiyeArrayMerge(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

}
