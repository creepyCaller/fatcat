package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.setting.ServerSetting;
import cn.edu.cuit.fatcat.setting.WebSetting;
import lombok.extern.slf4j.Slf4j;
import java.io.File;

/**
 * Fatcat容器入口方法
 *
 * @author  fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
        // 如果什么都没设置，则默认为"-port 8080"
        if ("-port".equals(args[0])) {
            WebSetting.port = Integer.parseInt(args[1]);
        }
        try {
            File wwwroot = new File(ServerSetting.WWWROOT);
            File error_path = new File(ServerSetting.WWWROOT + "/error");
            File error_page = new File(ServerSetting.WWWROOT + WebSetting.ERROR_PAGE);
            File index = new File(ServerSetting.WWWROOT + WebSetting.INDEX);
            if (!wwwroot.exists()) {
                if (wwwroot.mkdir()) {
                    log.warn("找不到网站根目录！创建文件夹" + wwwroot.getAbsolutePath());
                }
            }
            if (!error_path.exists()) {
                if (error_path.mkdirs()) {
                    System.out.println();
                    log.warn("找不到存放错误提示页的目录!创建文件夹" + error_path.getAbsolutePath());
                }
            }
            if(!error_page.exists()) {
                log.warn("找不到错误提示页! 请将错误提示页放置至" + ServerSetting.WWWROOT + WebSetting.ERROR_PAGE);
            }
            if(!index.exists()) {
                if (index.createNewFile()) {
                    log.warn("找不到导航页!创建文件" + index.getAbsolutePath());
                }
            }
            (new Thread(new Server(WebSetting.port))).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
