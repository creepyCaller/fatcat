package cn.edu.cuit.fatcat.launcher;

import cn.edu.cuit.linker.Server;
import cn.edu.cuit.fatcat.setting.FatcatSetting;
import cn.edu.cuit.fatcat.setting.WebApplicationServerSetting;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;

/**
 * Fatcat容器入口方法
 *
 * @author  fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Slf4j
public class Launch {

    public static void main(String[] args) {
        // 如果什么都没设置，则默认为"-port 80"
        long startTime = System.currentTimeMillis();
        try {
            if ("-port".equals(args[0])) {
                FatcatSetting.port = Integer.parseInt(args[1]);
            }
        } catch (Throwable t) {
            log.warn("未设置服务器监听端口，使用默认值：" + FatcatSetting.port);
        }
        try {
            log.info("正在开始初始化...");
            // 打印一大堆信息
            (new Launch()).init();
            log.info("服务器初始化成功!");
            log.info("正在启动服务器...");
            (new Thread(new Server())).start();
            log.info("服务器启动成功!共耗时" + (System.currentTimeMillis() - startTime) + "毫秒");
        } catch (Exception e) {
            log.error("服务器启动失败!");
            e.printStackTrace();
        }
    }

    /**
     * 仅用于检查文件，可有可无
     */
    private void init() throws IOException {
        File webapp = new File(FatcatSetting.WEB_APPLICATION);
        if (!webapp.exists()) {
            if (webapp.mkdir()) {
                log.warn("找不到网站根目录!创建文件夹" + webapp.getAbsolutePath());
            } else {
                log.error("找不到网站根目录!创建文件夹失败!");
                throw new IOException();
            }
        }
        File errorPath = new File(FatcatSetting.WEB_APPLICATION + "/error");
        if (!errorPath.exists()) {
            if (errorPath.mkdirs()) {
                log.warn("找不到存放错误提示页的目录!创建文件夹" + errorPath.getAbsolutePath());
            } else {
                log.error("找不到存放错误提示页的目录!创建文件夹失败!");
                throw new IOException();
            }
        }
        File error_page = new File(FatcatSetting.WEB_APPLICATION + WebApplicationServerSetting.ERROR_PAGE);
        if(!error_page.exists()) {
            log.warn("找不到错误提示页! 请将错误提示页放置至" + FatcatSetting.WEB_APPLICATION + WebApplicationServerSetting.ERROR_PAGE);
        }
        File index = new File(FatcatSetting.WEB_APPLICATION + WebApplicationServerSetting.INDEX);
        if(!index.exists()) {
            if (index.createNewFile()) {
                log.warn("找不到导航页!创建文件" + index.getAbsolutePath());
            } else {
                log.error("找不到导航页!创建文件失败!");
                throw new IOException();
            }
        }
    }

}
