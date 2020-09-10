package me.lqw.blog8.file.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 用于取出 ffmpeg 线程执行过程中产生的各种输出和错误流的信息
 * 不读取的话，会占满 jvm 内存，导致假死
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class PrintStream extends Thread {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    private final InputStream inputStream;
    private BufferedReader bufferedReader = null;
    private StringBuffer stringBuffer;

    public PrintStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            if (null == inputStream) {
                logger.error("--- 读取输出流出错！因为当前输出流为空！---");
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                logger.info(line);
                stringBuffer.append(line);
            }
        } catch (Exception e) {
            logger.error("--- 读取输入流出错了！--- 错误信息：" + e.getMessage());
        } finally {
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.error("--- 调用PrintStream读取输出流后，关闭流时出错！---");
            }
        }
    }

    public StringBuffer getStringBuffer() {
        return new StringBuffer();
    }
}