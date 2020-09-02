package me.lqw.blog8.web.controller.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

/**
 * 抽象父类 controller
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public abstract class AbstractBaseController {

    /**
     * 日志操作
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 获取 modelAndView
     *
     * @return ModelAndView
     */
    protected ModelAndView getModelAndView() {
        return new ModelAndView();
    }
}
