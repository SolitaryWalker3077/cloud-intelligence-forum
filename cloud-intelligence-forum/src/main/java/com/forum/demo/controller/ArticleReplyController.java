package com.forum.demo.controller;

import com.forum.demo.common.AppResult;
import com.forum.demo.common.ResultCode;
import com.forum.demo.config.AppConfig;
import com.forum.demo.model.Article;
import com.forum.demo.model.ArticleReply;
import com.forum.demo.model.User;
import com.forum.demo.service.IArticleReplyService;
import com.forum.demo.service.IArticleService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "回复接口")
@RestController
@RequestMapping("/reply")
public class ArticleReplyController {

    @Resource
    private IArticleService articleService;
    @Resource
    private IArticleReplyService articleReplyService;


    @PostMapping("/create")
    public AppResult create(HttpServletRequest request,
                            @Parameter(description = "帖子id") @RequestParam("articleId") @NonNull Long articleId,
                            @Parameter(description = "帖子内容") @RequestParam("content") @NonNull String content) {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        if(user.getState() == 1) {
            // 表示已禁言
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        Article article = articleService.selectById(articleId);
        // 是否存在，或已删除
        if (article == null || article.getDeleteState() == 1) {
            // 表示已删除或不存在
            return AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS);
        }
        // 是否封帖
        if (article.getState() == 1) {
            // 表示已封贴
            return AppResult.failed(ResultCode.FAILED_ARTICLE_BANNED);
        }

        // 构建回复对象
        ArticleReply articleReply = new ArticleReply();
        articleReply.setArticleId(articleId); // 要回复的帖Id
        articleReply.setPostUserId(user.getId()); // 回复的发送者
        articleReply.setContent(content); // 回复的内容
        // 写入回复
        articleReplyService.create(articleReply);
        // 返回结果
        return AppResult.success();
    }
}
