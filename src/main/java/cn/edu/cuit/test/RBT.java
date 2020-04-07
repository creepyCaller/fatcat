package cn.edu.cuit.test;

import java.util.ResourceBundle;

public class RBT {
    private static final String LSTRING_FILE = "javax.servlet.LocalStrings";
    private static ResourceBundle lStrings =
            ResourceBundle.getBundle(LSTRING_FILE);
    public static void main(String[] args) {
        System.out.println(lStrings);
    }
}
