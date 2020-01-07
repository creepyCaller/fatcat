package cn.edu.cuit.linker.util;

import cn.edu.cuit.fatcat.setting.FatcatSetting;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Objects;

/**
 * 文件工具类，处理文件相关
 *
 * @author fpc
 * @date 2019/10/24
 * @since Fatcat 0.0.1
 */
@Slf4j
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

    /**
     * 清空服务器根目录
     */
    public static void clearServerRoot() {
        log.info("开始清空服务器根目录");
        File serverRoot = new File(FatcatSetting.SERVER_ROOT);
        FileUtil.directoryHandler(serverRoot);
        log.info("清空目录成功");
    }

    private static void directoryHandler(File dir) {
        File[] files = Objects.requireNonNull(dir.listFiles());
        for (File iter : files) {
            if (iter.isFile()) {
                FileUtil.fileHandler(iter);
            } else if (iter.isDirectory()) {
                FileUtil.directoryHandler(iter);
                iter.delete();
            }
        }
    }

    private static void fileHandler(File file) {
        file.delete();
    }
}
