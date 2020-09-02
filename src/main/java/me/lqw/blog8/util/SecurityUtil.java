package me.lqw.blog8.util;


import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 安全工具类
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class SecurityUtil {

    /**
     * 构造方法
     */
    private SecurityUtil() {
        super();
    }

    /**
     * 加密算法
     * @param password password
     * @return string
     */
    public static String encodePasswordUseMd5(String password) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");

            digest.digest(password.getBytes(Charset.defaultCharset()));

            digest.update(password.getBytes(Charset.defaultCharset()));

            byte[] bytes = digest.digest();

            return Arrays.toString(bytes);

        } catch (NoSuchAlgorithmException e) {
            //理论上来说，不存在此错误
            e.printStackTrace();
        }
        return null;
    }
}
