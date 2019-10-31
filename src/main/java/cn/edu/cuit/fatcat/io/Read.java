package cn.edu.cuit.fatcat.io;

import cn.edu.cuit.fatcat.setting.ServerSetting;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 对输入的处理类
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
@Slf4j
public class Read {

    private InputStream iStr;

    public Read(InputStream iStr) {
        this.iStr = iStr;
    }

    /**
     * 读取一次输入的流
     *
     * @return 输入流
     * @throws IOException IO异常
     */
    public byte[] read() throws IOException {
        return read(iStr);
    }

    /**
     * 读取direction指定的文件的流
     *
     * @param direction 在WWWROOT下的目标读取文件
     * @return 读取出来的，现在只读网页，所以传出去是String，还没想好图片或者音乐视频怎么办
     * @throws IOException IO异常
     */
    public byte[] read(String direction) throws IOException {
        File file = new File(ServerSetting.WWWROOT + direction);
        log.info("读取文件: " + file.getAbsolutePath());
        FileInputStream fIStr = new FileInputStream(file);
        return read(fIStr);
    }

    /**
     * 从指定输入路读取
     *
     * @param is 指定的输入流，可以是套接字的输入流也可以是文件的
     * @return 流对应的byte数组
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

    public void close() throws IOException {
        this.iStr.close();
    }

}
