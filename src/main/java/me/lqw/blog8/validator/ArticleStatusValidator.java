package me.lqw.blog8.validator;

import me.lqw.blog8.model.enums.ArticleStatusEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * 状态校验
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class ArticleStatusValidator implements ConstraintValidator<ArticleStatus, ArticleStatusEnum> {

    @Override
    public void initialize(ArticleStatus constraintAnnotation) {

    }

    /**
     * 判断状态入参是否有效
     *
     * @param articleStatusEnum          statusEnum
     * @param constraintValidatorContext constraintValidatorContext
     * @return false | true
     */
    @Override
    public boolean isValid(ArticleStatusEnum articleStatusEnum, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.asList(ArticleStatusEnum.values()).contains(articleStatusEnum);
    }
}
