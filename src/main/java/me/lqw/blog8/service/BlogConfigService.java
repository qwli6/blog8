package me.lqw.blog8.service;

import com.fasterxml.jackson.core.type.TypeReference;
import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.mapper.BlogConfigMapper;
import me.lqw.blog8.model.BlogConfig;
import me.lqw.blog8.model.config.BlogConfigModel;
import me.lqw.blog8.model.config.CheckStrategy;
import me.lqw.blog8.model.config.ConfigModel;
import me.lqw.blog8.model.config.EmailConfigModel;
import me.lqw.blog8.model.enums.CommentCheckStrategy;
import me.lqw.blog8.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 博客设置系实现类
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Service
public class BlogConfigService {

    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 配置 Mapper
     */
    private final BlogConfigMapper configMapper;

    /**
     * 构造方法
     *
     * @param configMapper configMapper
     */
    public BlogConfigService(BlogConfigMapper configMapper) {
        this.configMapper = configMapper;
    }

    /**
     * 获取当前评论审核策略
     *
     * @return CommentCheckStrategy
     */
    @Transactional(readOnly = true)
    public Optional<CommentCheckStrategy> selectCurrentCheckStrategy() {

        Optional<BlogConfig> checkStrategyOp = configMapper.selectByKey(BlogConstants.COMMENT_CHECK_STRATEGY);
        if (checkStrategyOp.isPresent()) {
            BlogConfig blogConfig = checkStrategyOp.get();
            String value = blogConfig.getValue();
            if (StringUtils.isEmpty(value)) {
                return Optional.of(CommentCheckStrategy.FIRST);
            }

            List<CheckStrategy<CommentCheckStrategy>> checkStrategies = JsonUtil.parseObject(value, new TypeReference<List<CheckStrategy<CommentCheckStrategy>>>() {
            });

            if (checkStrategies == null || checkStrategies.isEmpty()) {

                return Optional.of(CommentCheckStrategy.FIRST);
            }

            Optional<CheckStrategy<CommentCheckStrategy>> any = checkStrategies.stream().filter(CheckStrategy::getActive).findAny();
            return any.map(commentCheckStrategyCheckStrategy -> Optional.of(commentCheckStrategyCheckStrategy.getT())).orElseGet(() -> Optional.of(CommentCheckStrategy.FIRST));

        }
        return Optional.of(CommentCheckStrategy.FIRST);
    }

    /**
     * 获取所有的评论审核策略
     *
     * @param key key
     * @return CommentCheckStrategy
     */
    @Transactional(readOnly = true)
    public List<CheckStrategy<CommentCheckStrategy>> selectAllCommentCheckStrategy(String key) {
        Optional<BlogConfig> checkStrategyOp = configMapper.selectByKey(key);
        if (checkStrategyOp.isPresent()) {
            BlogConfig blogConfig = checkStrategyOp.get();
            String value = blogConfig.getValue();
            if (StringUtils.isEmpty(value)) {
                return Collections.emptyList();
            }
            List<CheckStrategy<CommentCheckStrategy>> checkStrategies = JsonUtil.parseObject(value, new TypeReference<List<CheckStrategy<CommentCheckStrategy>>>() {
            });

            if (checkStrategies == null || checkStrategies.isEmpty()) {

                return Collections.emptyList();
            }

            return checkStrategies;
        }
        return Collections.emptyList();
    }

    public BlogConfigModel selectBlogConfig(String blogConfig) {
        Optional<BlogConfig> blogConfigOp = configMapper.selectByKey(blogConfig);
        if (blogConfigOp.isPresent()) {
            BlogConfig config = blogConfigOp.get();
            String value = config.getValue();
            if (StringUtils.isEmpty(value)) {
                return createDefaultBlogConfigModel();
            }

            BlogConfigModel blogConfigModel = JsonUtil.parseObject(value, new TypeReference<BlogConfigModel>() {
            });

            if (blogConfigModel == null) {
                return createDefaultBlogConfigModel();
            }
            return blogConfigModel;
        }
        return createDefaultBlogConfigModel();
    }


    /**
     * 创建默认的博客配置
     *
     * @return blogConfigModel
     */
    private BlogConfigModel createDefaultBlogConfigModel() {

        BlogConfigModel blogConfigModel = new BlogConfigModel();
        blogConfigModel.setFavicon("/static/images/favicon.ico");
        blogConfigModel.setHeader("博客系统");
        blogConfigModel.setFooterScript("");
        blogConfigModel.setWebsiteUrl("http://localhost:8080");
        logger.info("createDefaultBlogConfigModel return:[{}]", JsonUtil.toJsonString(blogConfigModel));
        return blogConfigModel;
    }

    /**
     * 更新博客配置
     *
     * @param configModel configModel
     * @throws LogicException 逻辑异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBlogConfig(BlogConfigModel configModel) throws LogicException {

        String value = JsonUtil.toJsonString(configModel);

        Assert.notNull(value, "config option value must not null.");

        updateConfig(BlogConstants.BLOG_CONFIG, value);

    }

    public void updateConfig(ConfigModel configModel) throws LogicException {
        String value = JsonUtil.toJsonString(configModel);

        Assert.notNull(value, "config option value must not null.");

        if (configModel instanceof BlogConfigModel) {
            updateConfig(BlogConstants.BLOG_CONFIG, value);
        }

        if (configModel instanceof EmailConfigModel) {
            updateConfig(BlogConstants.BLOG_CONFIG_EMAIL, value);
        }
    }

    /**
     * 更新系统配置
     *
     * @param key   系统级 key
     * @param value 系统配置 value
     * @throws LogicException 逻辑异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateConfig(String key, String value) throws LogicException {
        BlogConfig blogConfig = configMapper.selectByKey(key)
                .orElseThrow(() -> new LogicException("blogConfig.notExists", "配置不存在"));

        String oldValue = blogConfig.getValue();
        if (value.equals(oldValue)) {
            return;
        }
        configMapper.updateConfig(key, value);
    }
}
