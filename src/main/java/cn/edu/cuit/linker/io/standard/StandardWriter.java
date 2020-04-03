package cn.edu.cuit.linker.io.standard;

import cn.edu.cuit.fatcat.RecycleAble;
import cn.edu.cuit.fatcat.http.HttpHeader;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import cn.edu.cuit.linker.io.Writer;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import cn.edu.cuit.linker.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

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

    private void write(byte[] responseHead, byte[] responseBody) throws IOException {
        oStr.write(responseHead);
        oStr.write(responseBody);
        oStr.flush();
    }

    @Override
    public void write(Request request, Response response) throws IOException {
        byte[] responseBody = FileUtil.readBinStr(request, response); // 响应体二进制流
        if (response.getHeader(HttpHeader.ACCEPT_RANGES) != null) {
            response.setHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(responseBody.length));
        }
        byte[] responseHead = response.getResponseHeadString().getBytes(FatcatSetting.CHARSET); // 响应头二进制流
        this.write(responseHead, responseBody); // 将响应报文头转为字符串后再转为Byte数组，再和响应体合并，最后使用流输出到浏览器
    }

    @Override
    public void close() throws IOException {
        this.oStr.close();
    }

    @Override
    public void recycle() {

    }
}
