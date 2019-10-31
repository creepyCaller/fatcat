package cn.edu.cuit.fatcat.page;

import cn.edu.cuit.fatcat.setting.WebSetting;

/**
 * 自带的错误页，在指定的错误页无法读取时候使用
 *
 * @author fpc
 * @date 2019/10/24
 * @since Fatcat 0.0.1
 */
public class ErrorPage {

    public static byte[] getEmbeddedErrorPageBytes(String title, int statusCode, String statusDescription, String request, String response) {
        return  ("<!DOCTYPE html>\r\n" +
                "<html lang=\"zh-CN\">\r\n" +
                "<head>\r\n" +
                "\t<meta charset=\"UTF-8\">\r\n" +
                "\t<title>" + title + " - fatcat</title>\r\n" +
                "</head>\r\n" +
                "<body>\r\n" +
                "\t<p><h1>" + statusCode + "</h1></p>\r\n" +
                "\t<p>" + statusDescription + "</p>\r\n" +
                "\t<hr/>" +
                "\t<p>请求报文：</p>\r\n" +
                "\t<span>" + request.replaceAll("\r\n", "<br/>") + "</span>\r\n" +
                "\t<hr/>" +
                "\t<p>响应报文头：</p>\r\n" +
                "\t<span>" + response.replaceAll("\r\n", "<br/>") + "</span>\r\n" +
                "\t<hr/>" +
                "\t<p>警告：这是fatcat自带的错误页,请看到此页后自行配置自定义错误页.</p>\r\n" +
                "</body>\r\n" +
                "</html>").getBytes(WebSetting.CHARSET);
    }

}
