package cn.edu.cuit.fatcat.embed;

import cn.edu.cuit.fatcat.setting.WebApplicationServerSetting;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.ResponseHead;

public class TestPage {

    public static byte[] getEmbeddedTestPageBytes(Request request, ResponseHead responseHead) {
        return ("<!DOCTYPE html>\r\n" +
                "<html lang=\"zh-CN\">\r\n" +
                "<head>\r\n" +
                "\t<meta charset=\"UTF-8\">\r\n" +
                "\t<title>TEST - fatcat</title>\r\n" +
                "</head>\r\n" +
                "<body>\r\n" +
                "\t<p>请求报文：</p>\r\n" +
                "\t<span>" + request.toString().replaceAll("\r\n", "<br/>\r\n") + "</span>\r\n" +
                "\t<hr/>" +
                "\t<p>请求参数：</p>\r\n" +
                "\t<span>" + request.getParamString().replaceAll("\r\n", "<br/>\r\n") + "</span>\r\n" +
                "\t<hr/>" +
                "\t<p>响应报文头：</p>\r\n" +
                "\t<span>" + responseHead.toString().replaceAll("\r\n", "<br/>\r\n") + "</span>\r\n" +
                "</body>\r\n" +
                "</html>").getBytes(WebApplicationServerSetting.CHARSET);
    }

}
