package me.lqw.blog8.file.exception;


/**
 * 文件存在异常
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class FileAlreadyExistsException extends FileException {

    /**
     * 构造函数
     * @param code code
     * @param message message
     */
    public FileAlreadyExistsException(String code, String message) {
        super(code, message);
    }
}
