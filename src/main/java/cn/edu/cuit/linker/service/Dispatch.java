package cn.edu.cuit.linker.service;

import cn.edu.cuit.linker.Server;
import cn.edu.cuit.linker.io.Cache;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.fatcat.setting.FatcatSetting;

import java.io.File;
import java.util.Objects;

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
                if (FatcatSetting.WELCOME_LIST.size() > 0) {
                    FatcatSetting.WELCOME_LIST.stream()
                            .filter(Dispatch::nonEmpty)
                            .forEach(each -> {
                                if (checkWelcomeFile(each)) {
                                    request.setDirection(each);
                                }
                            });
                } else {
                    request.setDirection(FatcatSetting.DEFAULT_WELCOME);
                }
                break;
            case "/test":
                request.setDirection("$TEST$");
                break;
            case "/servlet":
                request.setDirection("$SERVLET$");
                break;
        }
    }

    private static boolean nonEmpty(String s) {
        return s != null && s.length() > 0;
    }

    private static boolean checkWelcomeFile(String welcome) {
        if (Cache.getInstance().get(welcome) != null) {
            return true;
        } else {
            File file = new File(FatcatSetting.SERVER_ROOT + welcome);
            return file.exists();
        }
    }
}
