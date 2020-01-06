package cn.edu.cuit.linker.util;

import cn.edu.cuit.fatcat.setting.FatcatSetting;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
public class WARUtil {

    public static void unpack(File srcFile) {
        try (ZipFile zipFile = new ZipFile(srcFile)) {
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    log.info("创建文件夹: {}", entry.getName());
                    File dir = new File(FatcatSetting.SERVER_ROOT, entry.getName());
                    dir.mkdirs();
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    log.info("解压文件: {}", entry.getName());
                    File targetFile = new File(FatcatSetting.SERVER_ROOT, entry.getName());
                    if (!targetFile.getParentFile().exists()) {
                        // 保证这个文件的父文件夹必须要存在
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[4096];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.close();
                    is.close();
                }
            }
        } catch (Throwable t) {
            log.error("解压错误: {}", t.toString());
            throw new RuntimeException();
        }
    }
}
