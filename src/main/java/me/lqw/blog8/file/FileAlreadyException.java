package me.lqw.blog8.file;

/**
 * 文件存在异常
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class FileAlreadyException extends FileException {


    public FileAlreadyException(String code, String message) {
        super(code, message);
    }
}
