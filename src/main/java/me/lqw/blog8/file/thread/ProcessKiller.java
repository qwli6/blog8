package me.lqw.blog8.file.thread;

/**
 * 在程序退出前结束已有的 FFmpeg 进程
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 *
 * */
public class ProcessKiller extends Thread {

    private final Process process;

    public ProcessKiller(Process process) {
        this.process = process;
    }

    @Override
    public void run() {
        this.process.destroy();
    }
}