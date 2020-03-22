package cn.edu.cuit.linker.message;

import cn.edu.cuit.fatcat.http.*;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * 响应报文实体
 *
 * @author fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    // **HEAD**

    private String protocol;

    private Integer code;

    private String status;

    // **PARAMS**

    private String contentType;

    private Date date;

    private PrintWriter printer; // 如果是servlet请求的话就使用PW

    private OutputStream outputStream;

    private Map<String, List<String>> header; // setHeader(String, String)

    // **SETTER**

    public void setHeader(String key, String value) {
        if (this.printer != null) {
            if (key != null && value != null) {
                this.printer.println(key + ": " + value);
            }
        }
        List<String> list = this.header.get(key);
        if (list != null) {
            list.add(value);
        } else {
            list = new ArrayList<>();
            list.add(value);
            this.header.put(key, list);
        }
    }

    public void setStatus(int code, String status) {
        // TODO:清空Printer缓冲区并重新生成并发送报文
        this.code = code;
        this.status = status;
    }

    public void setCode(int code) {
        // TODO:清空Printer缓冲区并重新生成并发送报文
        this.code = code;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
        if (this.getPrinter() != null) {
            this.printer.println("Content-Type: " + contentType);
        }
    }

    public void setCharacterEncoding(String charset) {
        this.setContentType(this.getContentType() + ";charset=" + charset);
    }

    // **Static Builder**

    public static Response standardResponseMessageHead() {
        return Response.builder()
                .protocol(HttpProtocol.HTTP_1_1)
                .code(HttpStatusCode.OK)
                .status(HttpStatusDescription.OK)
                .contentType(HttpContentType.TEXT_HTML)
                .date(new Date())
                .printer(null)
                .header(new HashMap<>())
                .build();
    }

    // **ToString**

//    public String getResponseHeadString() {
//        return protocol + " " + code + " " + status + "\r\n" +
//                this.getParamString() + "\r\n" + // 把所有参数封装在里边
//                "\r\n";
//    }

    public String getResponseHeadString() {
        return protocol + " " + code + " " + status + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Date: " + date.toString() + "\r\n" +
                "Server: Fatcat\r\n" +
                this.getParamString() +
                "\r\n";
    }

    private String getParamString() {
        if (header != null) {
            StringBuilder paramString = new StringBuilder();
            header.forEach((key, value) -> value.forEach(each -> paramString.append(key).append(": ").append(each).append("\r\n")));
            return paramString.toString();
        }
        return "";
    }
}
