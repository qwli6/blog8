package me.lqw.blog8.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 扩展默认的校验解析
 * Spring 对默认的校验错误展示不够友好，通过扩展
 *
 * @author liqiwen
 * @version 1.2
 * @see org.springframework.context.support.DefaultMessageSourceResolvable 来友好的展示我们里面的错误信息
 * @since 1.2
 */
public class ExtendsDefaultMessageSourceResolvable implements MessageSourceResolvable, Serializable {


    /**
     * 默认返回的 codes 信息
     */
    @Nullable
    private final String[] codes;
    @Nullable
    private final Object[] arguments;
    @Nullable
    private final String defaultMessage;

    public ExtendsDefaultMessageSourceResolvable(String code) {
        this(new String[]{code}, null, null);
    }

    public ExtendsDefaultMessageSourceResolvable(String[] codes) {
        this(codes, null, null);
    }

    public ExtendsDefaultMessageSourceResolvable(String[] codes, String defaultMessage) {
        this(codes, null, defaultMessage);
    }

    public ExtendsDefaultMessageSourceResolvable(String[] codes, Object[] arguments) {
        this(codes, arguments, null);
    }

    public ExtendsDefaultMessageSourceResolvable(@Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage) {
        this.codes = codes;
        this.arguments = arguments;
        this.defaultMessage = defaultMessage;
    }

    public ExtendsDefaultMessageSourceResolvable(MessageSourceResolvable resolvable) {
        this(resolvable.getCodes(), resolvable.getArguments(), resolvable.getDefaultMessage());
    }

    @Nullable
    public String getCode() {
        return this.codes != null && this.codes.length > 0 ? this.codes[this.codes.length - 1] : null;
    }

    @Nullable
    public String[] getCodes() {
        return this.codes;
    }

    @Nullable
    public Object[] getArguments() {
        return this.arguments;
    }

    @Nullable
    public String getDefaultMessage() {
        return this.defaultMessage;
    }

    public boolean shouldRenderDefaultMessage() {
        return true;
    }

    protected final String resolvableToString() {
        StringBuilder result = new StringBuilder(64);
        result.append("codes [").append(StringUtils.arrayToDelimitedString(this.codes, ","));
        result.append("]; arguments [").append(StringUtils.arrayToDelimitedString(this.arguments, ","));
        result.append("]; default message [").append(this.defaultMessage).append(']');
        return result.toString();
    }

    public String toString() {
        return this.getClass().getName() + ": " + this.resolvableToString();
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof MessageSourceResolvable)) {
            return false;
        } else {
            MessageSourceResolvable otherResolvable = (MessageSourceResolvable) other;
            return ObjectUtils.nullSafeEquals(this.getCodes(), otherResolvable.getCodes()) && ObjectUtils.nullSafeEquals(this.getArguments(), otherResolvable.getArguments()) && ObjectUtils.nullSafeEquals(this.getDefaultMessage(), otherResolvable.getDefaultMessage());
        }
    }

    public int hashCode() {
        int hashCode = ObjectUtils.nullSafeHashCode(this.getCodes());
        hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.getArguments());
        hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.getDefaultMessage());
        return hashCode;
    }
}
