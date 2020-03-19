package cn.edu.cuit.fatcat.container.servlet;

import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;

public interface Servlet {
    public void service(Request request, Response response);
}
