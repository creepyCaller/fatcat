package cn.edu.cuit.fatcat.service;

import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.fatcat.setting.FatcatSetting;

/**
 * 转发服务
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
public class DispatcherService {

    /**
     * 请求路径转发
     *
     * @param request 请求报文
     */
    public void dispatcher(Request request) {
        String requestDirection = request.getDirection();
        switch (requestDirection) {
            case "/":
                // 如果请求的路径是"/"，则转到欢迎页
                request.setDirection(FatcatSetting.INDEX);
                break;
            case "/test":
                request.setDirection("/TEST.html");
                break;
            case "/favicon.ico":
                request.setDirection(FatcatSetting.FAVICON);
            default:

                break;
        }
    }

}
