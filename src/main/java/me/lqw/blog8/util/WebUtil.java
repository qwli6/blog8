package me.lqw.blog8.util;

import me.lqw.blog8.constants.BlogConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 * WebUtils 相关工具类
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class WebUtil {

    /**
     * 日志 Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(WebUtil.class);

    /**
     * 本地 ip
     */
    private static final List<String> LOCAL_IP = Arrays.asList("0:0:0:0:0:0:0:1", "127.0.0.1");

    /**
     * 构造方法
     */
    private WebUtil() {
        super();
    }


    /**
     * 是否授权请求
     *
     * @param request request
     * @return false | true
     */
    public static boolean isAuthRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith(BlogConstants.CONSOLE_URI);
    }

    /**
     * 获取 ip 地址
     *
     * @param request request
     * @return string
     */
    public static String getIp(HttpServletRequest request) {

        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                //以下是后期添加的 要是不想在数据库看到 0:0:0.....或者 127.0.0.1的 数字串可用下边方法 亲测
                //根据网卡取本机配置的IP
                if (LOCAL_IP.contains(ip)) {
                    InetAddress inetAddress;
                    try {
                        inetAddress = InetAddress.getLocalHost();
                        ip = inetAddress.getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String strIp : ips) {
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }


    public static String getDomain(HttpServletRequest request){
        String path = request.getContextPath();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();// + path;
    }
}
