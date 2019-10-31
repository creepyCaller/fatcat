package cn.edu.cuit.fatcat.http;

/**
 * HTTP状态描述
 *
 * @author fpc
 * @date 2019/10/24
 * @since Fatcat 0.0.1
 */
public interface HttpStatusDescription {
    public static final String OK = "OK";
    public static final String NOT_FOUND = "Not Found";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String NOT_IMPLEMENTED = "Not Implemented";
    public static final String HTTP_VERSION_NOT_SUPPORTED = "HTTP Version not supported";
}