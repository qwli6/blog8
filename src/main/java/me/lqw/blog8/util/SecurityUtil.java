package me.lqw.blog8.util;

import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 安全检查
 * @author liqiwen
 * @since 1.2
 * @version 1.2
 */
public class SecurityUtil {
    private SecurityUtil() {
    }


    public static String encodePassword(String password, int length) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");

            digest.update(password.getBytes(Charset.defaultCharset()));

            byte[] bytes = digest.digest();


            int i = 0;
            StringBuilder stringBuffer = new StringBuilder();
            for(int offset = 0; offset < bytes.length; i++){
                i = bytes[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(i));//通过Integer.toHexString方法把值变为16进制
            }
            return stringBuffer.toString().substring(0, length);//从下标0开始，length目的是截取多少长度的值

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
