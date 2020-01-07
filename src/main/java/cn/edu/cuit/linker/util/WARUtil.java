package cn.edu.cuit.linker.util;

import cn.edu.cuit.fatcat.setting.FatcatSetting;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 解压WAR包的工具
 * 2020.1.7
 * @author fpc
 */
public class WARUtil {

    public static void unpack(File srcFile) throws IOException {
        boolean flag;
        ZipFile zipFile = new ZipFile(srcFile);
        Enumeration<?> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            // 如果是文件夹，就创建个文件夹
            if (entry.isDirectory()) {
                File dir = new File(FatcatSetting.SERVER_ROOT, entry.getName());
                if (!dir.exists()) {
                    flag = dir.mkdirs();
                    while (!flag) {
                        flag = dir.mkdirs();
                    }
                }
            } else {
                // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                File targetFile = new File(FatcatSetting.SERVER_ROOT, entry.getName());
                if (!targetFile.getParentFile().exists()) {
                    // 保证这个文件的父文件夹必须要存在
                    flag = targetFile.getParentFile().mkdirs();
                    while (!flag) {
                        flag = targetFile.getParentFile().mkdirs();
                    }
                }
                flag = targetFile.createNewFile();
                while (!flag) {
                    flag = targetFile.createNewFile();
                }
                InputStream is = zipFile.getInputStream(entry);
                FileOutputStream fos = new FileOutputStream(targetFile);
                int len;
                byte[] buf = new byte[1024 * 1024]; // 缓冲区设置为1MB
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.close();
                is.close();
            }
        }
        zipFile.close();
    }
}
