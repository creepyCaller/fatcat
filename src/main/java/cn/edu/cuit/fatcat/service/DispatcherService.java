package cn.edu.cuit.fatcat.service;

import cn.edu.cuit.fatcat.setting.Web;

/**
 * 转发服务
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
class DispatcherService {

    /**
     * 请求路径转发
     *
     * @param direction 待转发的路径
     * @return 转发后的路径或需要调用的Servlet
     */
    String dispatcher(String direction) {
        // 以后要处理访问Servlet的Request
        // 先找Servlet再找文件？
        // 找到Servlet以后就调用它，最后就把Servlet的Response写到浏览器端
        // 没有对应的Servlet就去WWWROOT下边找文件
        if ("/".equals(direction)) {
            // 如果请求的路径是"/"，则转到欢迎页(现在只是暂时设置为内部属性，以后要在settings里和Servlet里规定)
            return Web.INDEX;
        }
        return "/";
    }

}
