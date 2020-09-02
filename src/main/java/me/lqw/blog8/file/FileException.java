package me.lqw.blog8.file;

import me.lqw.blog8.constants.Message;
import me.lqw.blog8.exception.AbstractBlogException;
import me.lqw.blog8.exception.LogicException;

/**
 * 文件异常
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class FileException extends LogicException {

    public FileException(String code, String message) {
        super(code, message);
    }
}


