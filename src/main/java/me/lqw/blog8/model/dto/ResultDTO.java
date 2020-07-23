package me.lqw.blog8.model.dto;

/**
 * 返回结果集处理
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class ResultDTO {
    public static CR<?> create(){
        return new CR<>();
    }

    public static CR<?> create(Object data){
        return new CR<>(data);
    }

    public static QR createQR(PageResult<?> pageResult){
        return new QR(pageResult);
    }
}
