package me.lqw.blog8.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * 状态校验
 * @author liqiwen
 * @version 1.0
 */
public class StatusValidator implements ConstraintValidator<Status, StatusEnum> {

    @Override
    public void initialize(Status constraintAnnotation) {

    }

    @Override
    public boolean isValid(StatusEnum statusEnum, ConstraintValidatorContext constraintValidatorContext) {

        return Arrays.stream(StatusEnum.values()).anyMatch(h -> statusEnum.getCode().startsWith(h.getCode()));
    }
}
