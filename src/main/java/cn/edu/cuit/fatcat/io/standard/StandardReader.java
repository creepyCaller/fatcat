package cn.edu.cuit.fatcat.io.standard;

import cn.edu.cuit.fatcat.RecycleAble;
import cn.edu.cuit.fatcat.Setting;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * 对输入的处理类
 * TODO: 使用nio优化
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
@Slf4j
public class StandardReader implements AutoCloseable, RecycleAble {
    private InputStream iStr;

    public StandardReader(InputStream iStr) {
        this.iStr = iStr;
    }

    public byte[] readBinStr() throws IOException {
        return this.read(this.iStr);
    }

    /**
     * 读取一次输入的流
     *
     * @return 输入流
     * @throws IOException IO异常
     */
    public String readText() throws IOException {
        return new String(this.read(this.iStr), Setting.CHARSET);
    }

    /**
     * 从指定输入流读取
     *
     * @param is 指定的输入流，可以是套接字的输入流也可以是文件的
     * @return 字节流数组
     * @throws IOException IO异常
     */
    private byte[] read(InputStream is) throws IOException {
        int length = StandardReader.getLength(is);
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
    private static int getLength(InputStream is) throws IOException {
        int length = is.available();
        while (length == 0) {
            length = is.available();
        }
        return length;
    }

    @Override
    public void close() throws IOException {
        this.iStr.close();
    }

    @Override
    public void recycle() {

    }
}
