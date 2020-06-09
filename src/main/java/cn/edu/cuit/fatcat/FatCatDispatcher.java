package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.container.servlet.ServletMapping;
import cn.edu.cuit.fatcat.io.Cache;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 转发、调度
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.1
 */
public enum FatCatDispatcher {
    INSTANCE;

    private final Map<String, String> dispatchMap = new HashMap<>();

    public void setDispatch(String srcDir, String dstDir) {
        dispatchMap.put(srcDir, dstDir);
    }

    public String getDispatch(String srcDir) {
        return dispatchMap.get(srcDir);
    }

    /**
     * 请求路径转发
     *
     * @param direction 路径
     */
    public String dispatch(String direction) {
        if ("/".equals(direction)) {
            //这是对于请求根目录的情况
            if (Setting.WELCOME_LIST.size() > 0) {
                AtomicReference<String> ret = new AtomicReference<>();
                Setting.WELCOME_LIST.stream()
                        .filter(FatCatDispatcher::nonEmpty)
                        .forEach(each -> {
                            if (checkWelcomeFile(each)) {
                                ret.set(each);
                            }
                        });
                return ret.get();
            } else {
                return Setting.DEFAULT_WELCOME;
            }
        } else {
            //除了根目录就看看有没有转发
            String dispatch = getDispatch(direction);
            if (dispatch != null) {
                return dispatch;
            }
        }
        return direction;
    }

    private static boolean nonEmpty(String s) {
        return s != null && s.length() > 0;
    }

    /**
     * 检查欢迎文件页否存在
     * @param welcome
     * @return
     */
    private static boolean checkWelcomeFile(String welcome) {
        if (Cache.INSTANCE.get(welcome) != null || ServletMapping.INSTANCE.getServletName(welcome) != null) {
            return true;
        } else {
            File file = new File(Setting.SERVER_ROOT + welcome);
            return file.exists();
        }
    }
}
