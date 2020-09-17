package me.lqw.blog8.job;

import me.lqw.blog8.BlogProperties;
import me.lqw.blog8.service.SimpleMailHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * 数据库数据备份 job
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 * @since 2.3
 */
@Component
public class DatabaseBackupJob implements DisposableBean {

    /**
     * 记录日志
     */
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    /**
     * 博客配置文件
     */
    private final BlogProperties blogProperties;

    /**
     * 邮件处理
     */
    private final SimpleMailHandler mailHandler;

    /**
     * 构造方法
     * @param blogProperties blogProperties
     * @param mailHandler mailHandler
     */
    public DatabaseBackupJob(BlogProperties blogProperties, SimpleMailHandler mailHandler) {
        this.blogProperties = blogProperties;
        this.mailHandler = mailHandler;
    }


    /**
     * 定时备份数据库
     * 每天晚上 22 点 45 分 3 秒执行一次，备份成功后将备份文件通过邮件发送至指定邮箱
     * @since 2.3
     */
    @Scheduled(cron = "3 45 22 * * ?")
    @Async("blogThreadPoolExecutor")
    public void backup() {

        boolean backupDb = blogProperties.isBackupDb();
        if(!backupDb){
            logger.info("DatabaseBackupJob backup has closed!");
            return;
        }

        String dbUser = blogProperties.getDbUser();
        String dbHost = blogProperties.getDbHost();
        String dbPass = blogProperties.getDbPassword();
        String dbPort = blogProperties.getDbPort();
        String dbName = blogProperties.getDbName();
        String backPath = blogProperties.getBackPath();

        String[] commands = new String[]{"mysqldump", dbName,
                "--default-character-set=utf8mb4", "-h" + dbHost, "-P" + dbPort,
                "-u" + dbUser , "-p" + dbPass, "--result-file=" + backPath};

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commands);

        Process process = null;
        try {
            process = processBuilder.start();
            process.waitFor();

            //获取线程输入流
            InputStream inputStream = process.getInputStream();
            //获取进程错误流
            InputStream errorStream = process.getErrorStream();
            //获取输出流
            OutputStream outputStream = process.getOutputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp;
            while ((temp = bufferedReader.readLine()) != null){
                //流中的内容需要读取出来，不然会阻塞 JVM
                logger.info("temp: [{}]", temp);
            }

            logger.info("backup.sql back finished!");

            File backSqlFile = new File(backPath);
            if(backSqlFile.exists()){

                logger.info("backup.sql file send file to admin!");
                boolean sendResult = mailHandler.sendMailAttach(backSqlFile);
                if(sendResult) {
                    logger.info("backup.sql file send file finished!");
                    boolean delete = backSqlFile.delete();
                    logger.info("file delete finished [{}]", delete);
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error("DatabaseBackupJob exception:[{}]", e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if(process != null && process.isAlive()){
                process.destroy();
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        logger.info("DatabaseBackupJob destroy()");
    }
}
