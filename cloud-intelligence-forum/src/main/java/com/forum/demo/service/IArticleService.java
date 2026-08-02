package com.forum.demo.service;

import com.forum.demo.model.Article;
import org.springframework.transaction.annotation.Transactional;

public interface IArticleService {

    /**
     * 发布帖子
     * @param article 要发布的帖子
     */
    @Transactional
    void create(Article article);

}
