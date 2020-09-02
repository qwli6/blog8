package me.lqw.blog8.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 文章状态入参注解
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ArticleStatusValidator.class)
@Documented
public @interface ArticleStatus {

    String message() default "ArticleStatus is not allowed.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
