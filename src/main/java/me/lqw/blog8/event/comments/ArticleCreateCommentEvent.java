package me.lqw.blog8.event.comments;

import me.lqw.blog8.event.AbstractBlogEvent;
import me.lqw.blog8.model.Article;

/**
 * 内容创建评论事件
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class ArticleCreateCommentEvent extends AbstractBlogEvent<Article> {

    /**
     * create Article event when create a comment
     *
     * @param source  source
     * @param article article
     */
    public ArticleCreateCommentEvent(Object source, Article article) {
        super(source, article);
    }

}
