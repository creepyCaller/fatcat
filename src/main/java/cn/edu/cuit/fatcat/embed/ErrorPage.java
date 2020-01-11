package cn.edu.cuit.fatcat.embed;

import cn.edu.cuit.fatcat.setting.FatcatSetting;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;

/**
 * 自带的错误页，在指定的错误页无法读取时候使用
 *
 * @author fpc
 * @date 2019/10/24
 * @since Fatcat 0.0.1
 */
public class ErrorPage {

    public static String getEmbeddedErrorPageBytes(Request request, Response response) {
        return ("<!DOCTYPE html>\r\n" +
                "<html lang=\"zh-CN\">\r\n" +
                "<head>\r\n" +
                "<meta charset=\"UTF-8\">\r\n" +
                "<title>" + response.getStatus() + " - fatcat</title>\r\n" +
                "</head>\r\n" +
                "<body>\r\n" +
                "<p><h1>" + response.getCode() + "</h1></p>\r\n" +
                "<p>" + response.getStatus() + "</p>\r\n" +
                "<hr/>\r\n" +
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
                "</html>");
    }

    public static String getTomcatEmbeddedErrorPageBytes(Request request, Response response) {
        return ("<!doctype html>\r\n" +
                "<html lang=\"zh-CN\">\r\n" +
                "<head>\r\n" +
                "  <title>HTTP Status " + response.getStatus() + " – Fatcat</title>\r\n" +
                "    <style type=\"text/css\">\r\n" +
                "      h1 {\r\n" +
                "          font-family: Tahoma, Arial, sans-serif;\r\n" +
                "          color: white;\r\n" +
                "          background-color: #525D76;\r\n" +
                "          font-size: 22px;\r\n" +
                "      }\r\n" +
                "  \r\n" +
                "      h2 {\r\n" +
                "          font-family: Tahoma, Arial, sans-serif;\r\n" +
                "          color: white;\r\n" +
                "          background-color: #525D76;\r\n" +
                "          font-size: 16px;\r\n" +
                "      }\r\n" +
                "  \r\n" +
                "      h3 {\r\n" +
                "          font-family: Tahoma, Arial, sans-serif;\r\n" +
                "          color: white;\r\n" +
                "          background-color: #525D76;\r\n" +
                "          font-size: 14px;\r\n" +
                "      }\r\n" +
                "  \r\n" +
                "      body {\r\n" +
                "          font-family: Tahoma, Arial, sans-serif;\r\n" +
                "          color: black;\r\n" +
                "          background-color: white;\r\n" +
                "      }\r\n" +
                "  \r\n" +
                "      b {\r\n" +
                "          font-family: Tahoma, Arial, sans-serif;\r\n" +
                "          color: white;\r\n" +
                "          background-color: #525D76;\r\n" +
                "      }\r\n" +
                "  \r\n" +
                "      p {\r\n" +
                "          font-family: Tahoma, Arial, sans-serif;\r\n" +
                "          background: white;\r\n" +
                "          color: black;\r\n" +
                "          font-size: 12px;\r\n" +
                "      }\r\n" +
                "  \r\n" +
                "      a {\r\n" +
                "          color: black;\r\n" +
                "      }\r\n" +
                "  \r\n" +
                "      a.name {\r\n" +
                "          color: black;\r\n" +
                "      }\r\n" +
                "  \r\n" +
                "      .line {\r\n" +
                "          height: 1px;\r\n" +
                "          background-color: #525D76;\r\n" +
                "          border: none;\r\n" +
                "      }\r\n" +
                "    </style>\r\n" +
                "</head>\r\n" +
                "<body>\r\n" +
                "  <h1>HTTP Status " + response.getStatus() + "</h1>\r\n" +
                "  <hr class=\"line\"/>\r\n" +
                "  <p><b>Type</b> Status Report</p>\r\n" +
                "  <p><b>Message</b> " + request.getDirection() + "</p>\r\n" +
                "  <p><b>Description</b> " + response.getStatus() + "</p>\r\n" +
                "  <hr class=\"line\"/>\r\n" +
                "  <h3>Fatcat</h3>\r\n" +
                "</body>\r\n" +
                "</html>");
    }

}
