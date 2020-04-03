package cn.edu.cuit.linker;

import cn.edu.cuit.linker.io.standard.StandardCache;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 转发服务
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
public class Dispatcher {

    private static final Map<String, String> dispatchMap = new HashMap<>();

    static {
        // TODO: 在Setting.yml初始化
    }

    public static void setDispatch(String srcDir, String dstDir) {
        Dispatcher.dispatchMap.put(srcDir, dstDir);
    }

    public static String getDispatch(String srcDir) {
        return Dispatcher.dispatchMap.get(srcDir);
    }

    /**
     * 请求路径转发
     *
     * @param request 请求报文
     */
    public static void dispatch(Request request) {
        String requestDirection = request.getDirection();
        if ("/".equals(requestDirection)) {
            //这是对于请求根目录的情况
            // TODO: 改进？
            if (FatcatSetting.WELCOME_LIST.size() > 0) {
                FatcatSetting.WELCOME_LIST.stream()
                        .filter(Dispatcher::nonEmpty)
                        .forEach(each -> {
                            if (checkWelcomeFile(each)) {
                                request.setDirection(each);
                            }
                        });
            } else {
                request.setDirection(FatcatSetting.DEFAULT_WELCOME);
            }
        } else {
            //除了根目录就看看有没有转发
            String dispatch = Dispatcher.getDispatch(requestDirection);
            if (dispatch != null) {
                request.setDirection(dispatch);
            }
        }
    }

    private static boolean nonEmpty(String s) {
        return s != null && s.length() > 0;
    }

    private static boolean checkWelcomeFile(String welcome) {
        if (StandardCache.getInstance().get(welcome) != null) {
            return true;
        } else {
            File file = new File(FatcatSetting.SERVER_ROOT + welcome);
            return file.exists();
        }
    }
}
