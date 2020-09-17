package me.lqw.blog8;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 博客配置文件
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Validated
@ConfigurationProperties(prefix = "blog.core")
@Component
public class BlogProperties {

    /**
     * 文件开关
     */
    private boolean fileEnable;

    /**
     * 启动时是否重构索引
     */
    private boolean rebuildIndexWhenStartup;


    /**
     * 二次认证加密
     */
    private String topSecret;

    /**
     * 是否备份 sql
     * @since 2.3
     */
    private boolean backupDb;

    /**
     * 数据库用户名
     * @since 2.3
     */
    private String dbUser;

    /**
     * 数据库密码
     * @since 2.3
     */
    private String dbPassword;

    /**
     * 数据库端口
     * @since 2.3
     */
    private String dbPort;

    /**
     * 数据库主机
     * @since 2.3
     */
    private String dbHost;

    /**
     * 文件备份地址
     * @since 2.3
     */
    private String backPath;

    /**
     * 数据库名称
     * @since 2.3
     */
    private String dbName;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getBackPath() {
        return backPath;
    }

    public void setBackPath(String backPath) {
        this.backPath = backPath;
    }

    public boolean isFileEnable() {
        return fileEnable;
    }

    public void setFileEnable(boolean fileEnable) {
        this.fileEnable = fileEnable;
    }

    public boolean isRebuildIndexWhenStartup() {
        return rebuildIndexWhenStartup;
    }

    public void setRebuildIndexWhenStartup(boolean rebuildIndexWhenStartup) {
        this.rebuildIndexWhenStartup = rebuildIndexWhenStartup;
    }

    public String getTopSecret() {
        return topSecret;
    }

    public void setTopSecret(String topSecret) {
        this.topSecret = topSecret;
    }


    public boolean isBackupDb() {
        return backupDb;
    }

    public void setBackupDb(boolean backupDb) {
        this.backupDb = backupDb;
    }
}
