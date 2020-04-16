package cn.edu.cuit.fatcat.io.io;

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

    private StandardReader(InputStream iStr) {
        this.iStr = iStr;
    }

    public static StandardReader getReader(InputStream iStr) {
        return new StandardReader(iStr);
    }

    public byte[] readBinStr() throws IOException {
        return read(iStr);
    }

    /**
     * 读取一次输入的流
     *
     * @return 输入流
     * @throws IOException IO异常
     */
    public String readRequestContext() throws IOException {
        byte[] read = read(iStr);
        if (read == null) {
            return null;
        }
        return new String(read, Setting.CHARSET);
    }

    /**
     * 从指定输入流读取
     *
     * @param is 指定的输入流，可以是套接字的输入流也可以是文件的
     * @return 字节流数组
     * @throws IOException IO异常
     */
    private byte[] read(InputStream is) throws IOException {
        int length = getLength(is);
        if (length == 0) {
            return null;
        }
        byte[] buf = new byte[length];
        int read = 0;
        while (read < length) {
            int len = is.read(buf, read, length - read);
            if (len == -1) {
                break;
            } else {
                read += len;
            }
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
        int length = 0;
        long start = System.currentTimeMillis();
        while (length == 0) {
            length = is.available();
            if (length == 0 && (System.currentTimeMillis() - start) >  Setting.CONNECTION_KEEP) {
                break;
            }
        }
        return length;
    }

    @Override
    public void close() throws IOException {
        iStr.close();
    }

    @Override
    public void recycle() {
        iStr = null;
    }
}
