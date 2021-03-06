package cn.edu.cuit.linker.util;

/**
 * 文件工具类，处理文件相关
 *
 * @author fpc
 * @date 2019/10/24
 * @since Fatcat 0.0.1
 */
public class FileUtil {

    /**
     * 获取一个文件名的后缀名，即最后一个点的右边内容
     *
     * @param FileName 传入文件名
     * @return 文件的后缀名
     */
    public static String getFileSuffix(String FileName) {
        String[] a = FileName.split("\\.");
        if (a.length < 2) {
            // 如果拆分出来的数组长度小于2，即无后缀
            return "";
        } else {
            // 获取最后那串子串
            return a[a.length - 1];
        }
    }

}
