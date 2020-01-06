package cn.edu.cuit.fatcat.http;

/**
 * HTTP状态码
 *
 * @author fpc
 * @date 2019/10/24
 * @since Fatcat 0.0.1
 */
public interface HttpStatusCode {
    public static final Integer OK = 200;
    public static final Integer NOT_FOUND = 404;
    public static final Integer INTERNAL_SERVER_ERROR = 500;
    public static final Integer NOT_IMPLEMENTED = 501;
    public static final Integer HTTP_VERSION_NOT_SUPPORTED = 505;
}