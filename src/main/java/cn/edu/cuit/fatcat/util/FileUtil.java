package cn.edu.cuit.fatcat.util;

import cn.edu.cuit.fatcat.Setting;
import cn.edu.cuit.fatcat.http.HttpContentType;
import cn.edu.cuit.fatcat.http.HttpHeader;
import cn.edu.cuit.fatcat.io.Cache;
import cn.edu.cuit.fatcat.io.io.StandardReader;
import cn.edu.cuit.fatcat.message.Request;
import cn.edu.cuit.fatcat.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.channels.FileChannel;
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
     * 根据文件后缀名返回响应内容类型
     *
     * @param name 文件名
     * @return 响应内容类型
     */
    public static String getMimeType(String name) {
        String[] a = name.split("\\.");
        if (a.length < 2) {
            // 如果拆分出来的数组长度小于2，即无后缀
            return "";
        } else {
            // 获取最后那串子串
            name = a[a.length - 1];
            switch (name) {
                case "css":
                    return HttpContentType.TEXT_CSS;
                case "js":
                    return HttpContentType.APPLICATION_JS;
                case "json":
                    return HttpContentType.APPLICATION_JSON;
                case "png":
                    return HttpContentType.IMAGE_PNG;
                case "jpg":
                case "jpeg":
                    return HttpContentType.IMAGE_JPEG;
                case "ico":
                    return HttpContentType.IMAGE_X_ICON;
                case "gif":
                    return HttpContentType.IMAGE_GIF;
                case "svg":
                    return HttpContentType.IMAGE_SVG;
                case "xml":
                    return HttpContentType.TEXT_XML;
                case "txt":
                    return HttpContentType.TEXT_PLAIN;
                case "html":
                case "htm":
                    return HttpContentType.TEXT_HTML;
                default:
                    return HttpContentType.APPLICATION_OCTET_STREAM;
            }
        }
    }

    /**
     * 清空服务器根目录
     */
    public static void clearServerRoot() {
        log.info("开始清空服务器根目录");
        File serverRoot = new File(Setting.SERVER_ROOT);
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
                File dir = new File(Setting.SERVER_ROOT, entry.getName());
                if (!dir.exists()) {
                    flag = dir.mkdirs();
                    while (!flag) {
                        flag = dir.mkdirs();
                    }
                }
            } else {
                // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                File targetFile = new File(Setting.SERVER_ROOT, entry.getName());
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
        File dest = new File(Setting.SERVER_ROOT, "favicon.ico");
        if (!dest.exists()) {
            File src = new File(Setting.DEFAULT_FAVICON);
            Files.copy(src.toPath(), dest.toPath());
        }
    }

    public static byte[] readBinStr(Request request, Response response) throws IOException {
        byte[] biStr;
        Cache.Entry entry = Cache.INSTANCE.get(request.getDispatchedDirection()); // 先尝试从cache获取
        if (entry == null) {
            // 如果cache里没有记录, 就尝试新增记录
            FileUtil.putToCache(request.getDispatchedDirection());
            // 如果新增记录途中没有抛出异常, 说明把文件放入cache成功了
            entry = Cache.INSTANCE.get(request.getDispatchedDirection());
        }
        biStr = entry.getContext(); // 读出direction路径下的文件
        String mimeType = FileUtil.getMimeType(request.getDispatchedDirection()); // 通过后缀名判断文件类型
        response.setContentType(mimeType); // 设置响应报文的Content-Type
        response.setHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(entry.getSize()));
        if (mimeType.startsWith("text/") || mimeType.startsWith("application/")) {
            // 如果是文本文件,就设置响应头的编码类型
            // TODO:这个判断需要改进（意思是多几个clause）
            response.setCharacterEncoding(Setting.CHARSET_STRING);
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
    public static void putToCache(String direction) throws IOException {
        File file = new File(Setting.SERVER_ROOT + direction);
        Cache.INSTANCE.put(direction, file);
    }

    public static byte[] readClassRawFromClazzName(String className) throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        sb.append(Setting.SERVER_ROOT).append(File.separatorChar).append("WEB-INF").append(File.separatorChar).append("classes").append(File.separatorChar);
        if (className.contains(".")) {
            sb.append(className.replace('.', File.separatorChar)).append(".class");
        } else {
            sb.append(className).append(".class");
        }
        String path = sb.toString();
        File f = new File(path);
        try (FileInputStream fIS = new FileInputStream(f);
             StandardReader reader = StandardReader.getReader(fIS)) {
            return reader.readBinStr();
        } catch (IOException e) {
            log.error("找不到字节码文件: {}", path);
            throw new ClassNotFoundException(className);
        }
    }

    public static Document getWebXML() {
        StringBuilder sb = new StringBuilder();
        sb.append(Setting.SERVER_ROOT).append(File.separatorChar).append("WEB-INF").append(File.separatorChar).append("web.xml");
        File webxml = new File(sb.toString());
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(webxml);
        } catch (Exception e) {
            log.error("读取web.XML失败");
        }
        return null;
    }
}
