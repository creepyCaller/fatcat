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
public class Dispatch {

    /**
     * 请求路径转发
     *
     * @param request 请求报文
     */
    public static void dispatch(Request request) {
        String requestDirection = request.getDirection();
        switch (requestDirection) {
            case "/":
                request.setDirection(FatcatSetting.DEFAULT_WELCOME);
                break;
            case "/test":
                request.setDirection("$TEST$");
                break;
            case "/servlet":
                request.setDirection("$SERVLET$");
                break;
        }
    }

}
