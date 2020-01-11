package cn.edu.cuit.fatcat.embed;

import cn.edu.cuit.fatcat.setting.FatcatSetting;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;

public class TestPage {

    public static byte[] getEmbeddedTestPageBytes(Request request, Response response) {
        return ("<!DOCTYPE html>\r\n" +
                "<html lang=\"zh-CN\">\r\n" +
                "<head>\r\n" +
                "<meta charset=\"UTF-8\">\r\n" +
                "<title>TEST - fatcat</title>\r\n" +
                "</head>\r\n" +
                "<body>\r\n" +
                "<p>请求报文：</p>\r\n" +
                "<span>" + "\r\n" +
                request.toString().replaceAll("\r\n", "<br/>\r\n") +
                "</span>\r\n" +
                "<hr/>" + "\r\n" +
                "<p>请求参数：</p>\r\n" +
                "<span>" + "\r\n" +
                request.getParamString().replaceAll("\r\n", "<br/>\r\n") +
                "</span>\r\n" +
                "</body>\r\n" +
                "</html>").getBytes(FatcatSetting.CHARSET);
    }

}
