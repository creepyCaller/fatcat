package cn.edu.cuit.linker.util;

import cn.edu.cuit.fatcat.embed.TestPage;
import cn.edu.cuit.fatcat.http.HttpStatusCode;
import cn.edu.cuit.fatcat.http.HttpStatusDescription;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import cn.edu.cuit.linker.handler.ExceptionHandler;
import cn.edu.cuit.linker.io.Cache;
import cn.edu.cuit.linker.io.Reader;
import cn.edu.cuit.linker.io.standard.StandardReader;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.util.Objects;

@Slf4j
public class FileReader {

    public static byte[] readBinStr(Request request, Response response) {
        byte[] biStr;
        try {
            if (Objects.equals(request.getDirection(), "$TEST$")) {
                // 如果是调用测试页的话
                biStr = TestPage.getEmbeddedTestPageBytes(request, response);
            } else {
                // 读出direction路径下的文件
                biStr = FileReader.readBinStr(request.getDirection());
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
        byte[] oBS = Cache.get(direction);
        if (oBS != null) {
            return oBS;
        } else {
            File file = new File(FatcatSetting.SERVER_ROOT + direction); // FileNotFoundException
            FileInputStream fIStr = new FileInputStream(file); // IOException
            Reader reader = new StandardReader(fIStr);
            oBS = reader.readBinStr();
            Cache.put(direction, oBS);
            return oBS;
        }
    }
}
