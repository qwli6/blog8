package me.lqw.blog8.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 文章 url name 校验
 * @author liqiwen
 * @since 1.4
 * @version 1.4
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = UrlNameValidator.class)
@Documented
public @interface UrlName {

    String message() default "urlName format is not allowed.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
