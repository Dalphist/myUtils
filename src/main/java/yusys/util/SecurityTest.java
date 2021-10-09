package yusys.util;

/**
 * @author DJF
 * @version 0.1.0
 * @Description
 * @create 2021-07-28 17:01
 * @since 0.1.0
 **/
public class SecurityTest {
    private static String encodeKey = "thanks,pig4cloud";
    public static void main(String[] args) {
        String password = "1234";
        String PWD = SecurityUtils.encryptAes(password, encodeKey);
        System.out.println(PWD);
    }
}
