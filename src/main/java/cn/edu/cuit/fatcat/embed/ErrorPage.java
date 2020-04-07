package cn.edu.cuit.fatcat.embed;

import cn.edu.cuit.fatcat.message.Request;
import cn.edu.cuit.fatcat.message.Response;

/**
 * 自带的错误页，在指定的错误页无法读取时候使用
 *
 * @author fpc
 * @date 2019/10/24
 * @since Fatcat 0.0.1
 */
public class ErrorPage {

    public static String getTomcatEmbeddedErrorPageBytes(Request request, Response response) {
        return ("<!doctype html>\r\n" +
                "<html>\r\n" +
                "<head>\r\n" +
                "  <title>HTTP Status " + response.getStatus() + "</title>\r\n" +
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
