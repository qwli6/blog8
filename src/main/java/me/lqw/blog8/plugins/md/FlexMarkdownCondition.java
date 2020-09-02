package me.lqw.blog8.plugins.md;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * FlexMarkdownCondition
 *
 * @author liqiwen
 * @version 1.3
 * @since 1.3
 */
public class FlexMarkdownCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        return "true".equals(environment.getProperty("blog.core.markdown.flex.enabled"));
    }
}
