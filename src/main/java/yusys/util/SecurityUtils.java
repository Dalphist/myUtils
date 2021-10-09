package yusys.util;

import cn.hutool.core.codec.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * 安全服务工具类
 * 
 * @author ruoyi
 */
public class SecurityUtils
{
    private static final String KEY_ALGORITHM = "AES";
    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public static String encryptAes(String data,String pass)  {

//        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding,//如果不使用PKCS5Padding 加密算法 会报data not block size aligned 数据块对不上得 异常错误
//         new SecretKeySpec(pass.getBytes(StandardCharsets.UTF_8), KEY_ALGORITHM),
//         new IvParameterSpec(pass.getBytes()));
//        byte[] dataencrypt = aes.encrypt(data);
//        return   Base64.encode(dataencrypt);


//        byte[] bytes = pass.getBytes(StandardCharsets.UTF_8);
//        SecretKeySpec skeySpec = new SecretKeySpec(bytes,"AES");
//        IvParameterSpec ivParameterSpec = new IvParameterSpec(pass.getBytes());
//
//        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding,skeySpec,ivParameterSpec);
//        byte[] encrypt = aes.encrypt(Base64.encode(data.getBytes(StandardCharsets.UTF_8)));
//
//
//        return new String(encrypt,StandardCharsets.UTF_8);

        byte[] raw = pass.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        try{
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
            IvParameterSpec iv = new IvParameterSpec(pass.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            String encode = Base64.encode(encrypted);
            return encode;
        }catch (Exception e){
            throw new RuntimeException(data+": 加密出错");
        }

    }

}
