package me.lqw.blog8.file;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

@Component
public class FileCondition implements Condition {


    // if true, condition anno will been initial
    @Override
    public boolean matches(ConditionContext conditionContext,
                           AnnotatedTypeMetadata annotatedTypeMetadata) {
        return "true".equals(conditionContext.getEnvironment().getProperty("blog.file.enabled"));
    }
}
