package me.lqw.blog8.web.aop;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 访问日志
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class BlogRequestLog implements Serializable {

    /**
     * 访问 url
     */
    private String url;

    /**
     * 访问 ip
     */
    private String ip;

    /**
     * 访问 classMethod
     */
    private String classMethod;

    /**
     * 访问入参
     */
    private Object[] args;

    /**
     * 构造方法
     */
    public BlogRequestLog() {
        super();
    }

    /**
     * 有参构造方法
     *
     * @param url         url
     * @param ip          ip
     * @param classMethod classMethod
     * @param args        args
     */
    public BlogRequestLog(String url, String ip, String classMethod, Object[] args) {
        super();
        this.url = url;
        this.ip = ip;
        this.classMethod = classMethod;
        this.args = args;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "RequestLog{" +
                "url='" + url + '\'' +
                ", ip='" + ip + '\'' +
                ", classMethod='" + classMethod + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}