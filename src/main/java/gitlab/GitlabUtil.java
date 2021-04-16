package gitlab;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import gitlab.entity.User;
import http.HttpUtil;

import java.util.List;
import java.util.Map;

/**
 * @author DJF
 * @version 0.1.0
 * @Description
 * @create 2020-09-14 10:16
 * @since 0.1.0
 **/
public class GitlabUtil {

    private static String token = "Ef2S6i2xbEHHq_k8QywR";
    private static String baseUrl = "http://192.168.251.107/api/v3";

    public static void main(String[] args) throws Exception {
//        List<User> userList = UserUtil.getUserList(baseUrl,null,token);
//        User user = User.builder().email("aaa@bbb.com").username("api1").name("api1").password("12345678").private_token(token).build();
//        String result = UserUtil.createUser(baseUrl,user);

        User user = User.builder().id("38").email("aaa@bbb1.com").username("api1").name("api1").password("123456780").private_token(token).build();
        String result = UserUtil.updateUser(baseUrl,user);
    }


}
