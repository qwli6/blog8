package me.lqw.blog8.model.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogConfigModel implements Serializable {


    /**
     * 博客标题
     */
    private String blogTitle;


    /**
     * 头部信息
     */
    private String header;

    /**
     * 网站根地址
     */
    private String websiteUrl;

    /**
     * 网站图标
     */
    private String favicon;


    /**
     * 底部脚本
     */
    private String footerScript;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getFooterScript() {
        return footerScript;
    }

    public void setFooterScript(String footerScript) {
        this.footerScript = footerScript;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }
}
