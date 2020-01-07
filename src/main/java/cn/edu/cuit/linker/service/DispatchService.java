package cn.edu.cuit.linker.service;

import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.fatcat.setting.FatcatSetting;

/**
 * 转发服务
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
public class DispatchService {

    /**
     * 请求路径转发
     *
     * @param request 请求报文
     */
    public void dispatch(Request request) {
        String requestDirection = request.getDirection();
        if ("/".equals(requestDirection)) {// 如果请求的路径是"/"，则转到欢迎页
            request.setDirection(FatcatSetting.DEFAULT_WELCOME);
        } else if ("/test".equals(requestDirection)) {
            request.setDirection("/TEST.html");
        }
    }

}
