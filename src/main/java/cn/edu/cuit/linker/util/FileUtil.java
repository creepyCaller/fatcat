package cn.edu.cuit.linker.util;

import cn.edu.cuit.fatcat.embed.TestPage;
import cn.edu.cuit.fatcat.http.HttpStatusCode;
import cn.edu.cuit.fatcat.http.HttpStatusDescription;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import cn.edu.cuit.linker.handler.ExceptionHandler;
import cn.edu.cuit.linker.io.Cache;
import cn.edu.cuit.linker.io.standard.StandardCache;
import cn.edu.cuit.linker.io.Reader;
import cn.edu.cuit.linker.io.standard.StandardReader;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

    /**
     * 解压WAR包
     * 2020.1.7
     * @author fpc
     */
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
        // 检测有没有图标,如果没有的话就把默认的图标复制过去
        File dest = new File(FatcatSetting.SERVER_ROOT, "favicon.ico");
        if (!dest.exists()) {
            File src = new File(FatcatSetting.DEFAULT_FAVICON);
            Files.copy(src.toPath(), dest.toPath());
        }
    }

    public static byte[] readBinStr(Request request, Response response) {
        byte[] biStr;
        try {
            if (Objects.equals(request.getDirection(), "$TEST$")) {
                // 如果是调用测试页的话
                biStr = TestPage.getEmbeddedTestPageBytes(request, response);
            } else {
                // 读出direction路径下的文件
                biStr = FileUtil.readBinStr(request.getDirection());
            }
        } catch (FileNotFoundException e) {
            // 404 Not Found
            log.info("找不到文件: {}", request.getDirection());
            biStr = ExceptionHandler.handleException(request, response, HttpStatusCode.NOT_FOUND, HttpStatusDescription.NOT_FOUND);
        } catch (IOException e) {
            // 500 Internal Server Error
            biStr = ExceptionHandler.handleException(request, response, HttpStatusCode.INTERNAL_SERVER_ERROR, HttpStatusDescription.INTERNAL_SERVER_ERROR);
        }
        return biStr;
    }

    /**
     * 读取direction指定的文件的流
     *
     * @param direction 在网站根目录下的目标读取文件
     * @return 文件字节流
     * @throws IOException IO异常
     */
    public static byte[] readBinStr(String direction) throws IOException {
        log.info("读取文件, 路径: {}", direction);
        byte[] oBS = Cache.getInstance().get(direction);
        if (oBS != null) {
            log.info("从缓存获取: {}, 成功! 比特流长度为: {}", direction, oBS.length);
            return oBS;
        } else {
            File file = new File(FatcatSetting.SERVER_ROOT + direction); // FileNotFoundException
            FileInputStream fIStr = new FileInputStream(file); // IOException
            Reader reader = new StandardReader(fIStr);
            oBS = reader.readBinStr();
            Cache.getInstance().put(direction, oBS);
            return oBS;
        }
    }
}
