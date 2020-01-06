package cn.edu.cuit.linker.io.standard;

import cn.edu.cuit.linker.io.Writer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 对输出的处理类
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
@Slf4j
public class StandardWriter implements Writer {

    private OutputStream oStr;

    public StandardWriter(OutputStream oStr) {
        this.oStr = oStr;
    }

    /**
     * 将ResponseMessage实体转为字符串，输出到浏览器
     *
     * @param bStr 输出流
     */
    @Override
    public void write(byte[] bStr) throws IOException {
        oStr.write(bStr);
        oStr.flush();
    }

    @Override
    public void close() throws IOException {
        this.oStr.close();
    }

}
