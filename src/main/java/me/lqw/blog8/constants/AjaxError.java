package me.lqw.blog8.constants;

import org.springframework.context.MessageSourceResolvable;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义 ajaxError 异常
 * 用于 jsr 校验异常包装
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.2
 */
public class AjaxError implements Serializable {

    private final List<MessageSourceResolvable> errors;

    public AjaxError(List<MessageSourceResolvable> errors) {
        super();
        this.errors = errors;
    }

    public List<MessageSourceResolvable> getErrors() {
        return errors;
    }
}
