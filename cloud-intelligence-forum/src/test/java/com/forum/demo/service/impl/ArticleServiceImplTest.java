package com.forum.demo.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forum.demo.model.Article;
import com.forum.demo.service.IArticleService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleServiceImplTest {

    @Resource
    private IArticleService articleService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    @Test
    void create() {
        Article article = new Article();
        article.setUserId(2L); // bitboy
        article.setBoardId(1L); // java版块
        article.setTitle("单元测试");
        article.setContent("测试内容");
        articleService.create(article);
        System.out.println("发贴成功");
    }

    @Test
    void selectAll() throws JsonProcessingException {
        //调用service
        List<Article> articles = articleService.selectAll();
        //转换成Json字符串打印
        System.out.println(objectMapper.writeValueAsString(articles));
    }
}