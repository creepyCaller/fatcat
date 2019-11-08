package cn.edu.cuit.fatcat.setting;

/**
 * 服务器配置
 * WEB_APPLICATION: 网站根目录
 * timeout: 连接超时自动断开（ms）
 * port：服务端口
 *
 * @author fpc
 * @date 2019/10/27
 * @since Fatcat 0.0.1
 */
public class FatcatSetting {

    // 临时解决方案：在打JAR包的时候换成"../webApplication"
    public static String WEB_APPLICATION = "WebApplication";

    public long timeout = 20000;

    public static int port = 80;

}
