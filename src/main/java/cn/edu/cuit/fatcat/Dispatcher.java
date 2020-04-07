package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.io.CacheImpl;
import cn.edu.cuit.fatcat.message.Request;

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
    private Dispatcher() {}

    private static final Map<String, String> dispatchMap = new HashMap<>();

    private static Dispatcher instance;

    public static Dispatcher getInstance() {
        if (instance == null) {
            instance = new Dispatcher();
        }
        return instance;
    }

    // TODO: 在Setting.yml设置转发

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
    public void dispatch(Request request) {
        String requestDirection = request.getDirection();
        if ("/".equals(requestDirection)) {
            //这是对于请求根目录的情况
            // TODO: 改进？
            if (Setting.WELCOME_LIST.size() > 0) {
                Setting.WELCOME_LIST.stream()
                        .filter(Dispatcher::nonEmpty)
                        .forEach(each -> {
                            if (checkWelcomeFile(each)) {
                                request.setDirection(each);
                            }
                        });
            } else {
                request.setDirection(Setting.DEFAULT_WELCOME);
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
        if (CacheImpl.getInstance().get(welcome) != null) {
            return true;
        } else {
            File file = new File(Setting.SERVER_ROOT + welcome);
            return file.exists();
        }
    }
}
