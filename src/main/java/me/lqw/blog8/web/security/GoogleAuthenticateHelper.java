package me.lqw.blog8.web.security;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import me.lqw.blog8.BlogProperties;
import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.exception.AbstractBlogException;
import me.lqw.blog8.exception.UnauthorizedException;
import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * google 认证器
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
@Component
public class GoogleAuthenticateHelper {


    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());


    private final BlogProperties blogProperties;

    public GoogleAuthenticateHelper(BlogProperties blogProperties) {
        this.blogProperties = blogProperties;
    }

    /**
     * 校验二次认证是否成功
     * @param googleTopCode googleTopCode
     * @return true | false
     * @throws AbstractBlogException
     * <p>
     *     1. 格式化异常
     * </p>
     */
    public boolean checkCode(GoogleTopCode googleTopCode) throws AbstractBlogException {

        String topCode = googleTopCode.getTopCode();

        Asserts.notBlank(topCode, "topCode must be not null.");

        if(topCode.length() < 6){
            //虚晃一枪, 不要让人知道长度是多少
            //只告诉它认证失败
            throw new UnauthorizedException(BlogConstants.AUTH_FAILED);
        }

        int authCode;
        try{
            authCode = Integer.parseInt(googleTopCode.getTopCode());
        } catch (NumberFormatException e){
            //格式化异常就不打印了
            logger.error("二次认证码: [{}]", googleTopCode.getTopCode());
            throw new UnauthorizedException(BlogConstants.AUTH_FAILED);
        }

        GoogleAuthenticatorKey.Builder googleAuthenticatorKey = new GoogleAuthenticatorKey.Builder(googleTopCode.getEmail());
        googleAuthenticatorKey.build();
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        return googleAuthenticator.authorize(blogProperties.getTopSecret(), authCode);
    }

}
