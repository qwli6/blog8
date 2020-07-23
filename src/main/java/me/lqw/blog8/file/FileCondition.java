package me.lqw.blog8.file;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

/**
 * 是否启用文件系统
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Component
public class FileCondition implements Condition {



    @Override
    public boolean matches(ConditionContext conditionContext,
                           AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        String fileEnabled = environment.getProperty("blog.file.enabled");
        return "true".equals(fileEnabled);
    }
}
