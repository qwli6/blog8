package me.lqw.blog8.file.exception;

/**
 * 文件不存在异常
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class FileNotExistsException extends FileException {

    /**
     * 构造方法
     * @param code code
     * @param message message
     */
    public FileNotExistsException(String code, String message) {
        super(code, message);
    }
}
