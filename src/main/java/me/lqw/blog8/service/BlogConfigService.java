package me.lqw.blog8.service;

import me.lqw.blog8.model.CommentCheckStrategy;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

@Service
public class BlogConfigService implements Serializable {

    public Optional<CommentCheckStrategy> findCurrentCheckStrategy(){
        return Optional.of(CommentCheckStrategy.FIRST);
    }

}
