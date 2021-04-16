package gitlab;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
 * @create 2020-10-16 15:54
 * @since 0.1.0
 **/
public class UserUtil {
    public static List<User> getUserList(String baseUrl,User user,String token) throws Exception {
        String url = baseUrl + "/users?private_token=" + token;
        String result = GitLabHttpUtil.doGet(url);
        JSONArray array = JSONArray.parseArray(result);
        List<User> userList = JSONObject.parseArray(array.toJSONString(), User.class);
        return userList;
    }

    public static String createUser(String baseUrl,User user) throws Exception {
        String url = baseUrl + "/users";
        Map<String, String> map = JSONObject.parseObject(JSON.toJSONString(user), new TypeReference<Map<String, String>>(){});
        String result = GitLabHttpUtil.doPost(url,map);
        System.out.println(result);
        return result;
    }

    public static String updateUser(String baseUrl,User user) throws Exception {
        String url = baseUrl + "/users/" + user.getId();
        Map<String, String> map = JSONObject.parseObject(JSON.toJSONString(user), new TypeReference<Map<String, String>>(){});
        String result = GitLabHttpUtil.doPut(url,map);
        System.out.println(result);
        return result;
    }
}
