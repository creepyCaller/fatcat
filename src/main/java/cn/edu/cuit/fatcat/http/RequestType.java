package cn.edu.cuit.fatcat.http;

/**
 * 请求类型
 * 请求的是一个文件还是一个Servlet
 * 或者是转发
 *
 * @author fpc
 * @date 2019/10/24
 * @since Fatcat 0.0.1
 */
public interface RequestType {

    public static final String FILE = "file";
    public static final String SERVLET = "servlet";
    public static final String DISPATCH = "dispatch";

}
