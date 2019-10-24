package cn.edu.cuit.fatcat;

/**
 * Fatcat容器入口方法
 *
 * @author  fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
public class Main {

    public static void main(String[] args) {
        try {
            (new Thread(new Server(8080))).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
