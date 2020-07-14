package me.lqw.blog8.service;

import me.lqw.blog8.exception.LogicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BaseService<T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public abstract T save(T t) throws LogicException;

    public abstract void delete(Integer id) throws LogicException;

    public abstract void update(T t) throws LogicException;
}
