package me.lqw.blog8.validator;

import me.lqw.blog8.model.Article;
import me.lqw.blog8.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * urlName 校验
 * @author liqiwen
 * @since 1.4
 * @version 1.4
 * 1. 不能包含 / 或者 \
 * 2. 不能包含空格
 * 3. 大写字母转换成小写字母
 */
public class UrlNameValidator implements ConstraintValidator<UrlName, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if(StringUtils.isBlank(s)){
            return true;
        }

        //包含斜杠
        if(s.contains("/")){
            return false;
        }

        //包含英文空格
        if(s.contains(" ")){
            return false;
        }

        return true;
    }
}
