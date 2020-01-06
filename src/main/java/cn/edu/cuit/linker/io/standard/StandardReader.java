package cn.edu.cuit.linker.io.standard;

import cn.edu.cuit.fatcat.setting.FatcatSetting;
import cn.edu.cuit.linker.io.Cache;
import cn.edu.cuit.linker.io.Reader;
import lombok.extern.slf4j.Slf4j;
import java.io.*;

/**
 * 对输入的处理类
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
@Slf4j
public class StandardReader implements Reader {

    private InputStream iStr;

    public StandardReader(InputStream iStr) {
        this.iStr = iStr;
    }

    /**
     * 读取一次输入的流
     *
     * @return 输入流
     * @throws IOException IO异常
     */
    @Override
    public byte[] read() throws IOException {
        return read(iStr);
    }

    /**
     * 读取direction指定的文件的流
     *
     * @param direction 在网站根目录下的目标读取文件
     * @return 文件字节流
     * @throws IOException IO异常
     */
    @Override
    public byte[] read(String direction) throws IOException {
        log.info("读取文件: " + direction);
        byte[] oBS = Cache.get(direction);
        if (oBS != null) {
            log.info("从缓存中获取: " + direction + ", 成功!");
            return oBS;
        } else {
            log.info("缓存中不存在: " + direction + ", 正在从硬盘中获取...");
            File file = new File(FatcatSetting.WEB_APPLICATION + direction); // FileNotFoundException
            FileInputStream fIStr = new FileInputStream(file); // IOException
            oBS = read(fIStr);
            Cache.put(direction, oBS);
            return oBS;
        }
    }

    /**
     * 从指定输入路读取
     *
     * @param is 指定的输入流，可以是套接字的输入流也可以是文件的
     * @return 字节流数组
     * @throws IOException IO异常
     */
    private byte[] read(InputStream is) throws IOException {
        int length = getLength(is);
        byte[] buf = new byte[length];
        int read = 0;
        while (read < length) {
            read += is.read(buf, read, length - read);
        }
        return buf;
    }

    /**
     * 获取指定流中需要读取的长度
     * 由于套接字是非实时的，所以需要不断请求
     *
     * @param is 指定输入流
     * @return 输入流可读长度
     * @throws IOException IO异常
     */
    private int getLength(InputStream is) throws IOException {
        int length = is.available();
        while (length == 0) {
            length = is.available();
        }
        return length;
    }

    @Override
    public void close() throws IOException {
        log.info("关闭输入流: {}", iStr.toString());
        this.iStr.close();
    }

}
