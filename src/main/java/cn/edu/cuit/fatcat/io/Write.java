package cn.edu.cuit.fatcat.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 对输出的处理类
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
public class Write {

    private OutputStream oStr;

    public Write(OutputStream oStr) {
        this.oStr = oStr;
    }

    /**
     * 将ResponseMessage实体转为字符串，输出到浏览器
     *
     * @param bStr 输出流
     */
    public void write(byte[] bStr) throws IOException {
        oStr.write(bStr);
        oStr.flush();
    }

    public void close() throws IOException {
        this.oStr.close();
    }

}
