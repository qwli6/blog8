package me.lqw.blog8.web.controller.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());



    protected ModelAndView getModelAndView(){

        return new ModelAndView();
    }
}
