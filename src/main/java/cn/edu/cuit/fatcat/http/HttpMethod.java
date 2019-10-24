package cn.edu.cuit.fatcat.http;

/**
 * HTTP方法(动作)
 *
 * @author fpc
 * @date 2019/10/24
 * @since Fatcat 0.0.1
 */
public interface HttpMethod {
    String METHOD_DELETE = "DELETE";
    String METHOD_HEAD = "HEAD";
    String METHOD_GET = "GET";
    String METHOD_OPTIONS = "OPTIONS";
    String METHOD_POST = "POST";
    String METHOD_PUT = "PUT";
    String METHOD_TRACE = "TRACE";
    String METHOD_PATCH = "PATCH";
}
