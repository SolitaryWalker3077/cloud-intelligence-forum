package com.forum.demo.controller;


import com.forum.demo.common.AppResult;
import com.forum.demo.common.ResultCode;
import com.forum.demo.config.AppConfig;
import com.forum.demo.model.Article;
import com.forum.demo.model.Board;
import com.forum.demo.model.User;
import com.forum.demo.service.IArticleService;
import com.forum.demo.service.IBoardService;
import com.forum.demo.service.impl.BoardServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Tag(name = "文章接口")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private IArticleService articleService;
    @Resource
    private IBoardService boardService;
    /**
     * 发布新帖子
     * @param boardId 版块Id
     * @param title 文章标题
     * @param content 文章内容
     * @return
     */
    @Operation(summary = "发布新贴")
    @PostMapping("create")
    public AppResult create(HttpServletRequest request,
                            @Parameter(description = "板块id") @RequestParam("boardId") @NonNull Long boardId,
                            @Parameter(description = "文章标题") @RequestParam("title") @NonNull String title,
                            @Parameter(description = "文章内容") @RequestParam("content") @NonNull String content) {


        //检测用户是否禁言
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute(AppConfig.USER_SESSION);
        if(user.getState() == 1) {
            //用户已禁言
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        //获取板块信息
        Board board = boardService.selectById(boardId.longValue());
        //校验
        if(board == null || board.getState() ==1 || board.getDeleteState() == 1) {
            // 打印日志
            log.warn(ResultCode.FAILED_BOARD_BANNED.toString());
            // 返回响应
            return AppResult.failed(ResultCode.FAILED_BOARD_BANNED);
        }
        //封装文章对象
        Article article = new Article();
        article.setTitle(title);//设置标题
        article.setContent(content);//设置文章内容
        article.setBoardId(boardId);//设置板块id
        article.setUserId(user.getId());//设置作者id
        //调用service
        articleService.create(article);

        return AppResult.success();
    }


    @Operation(summary = "根据版块Id查询帖⼦列表")
    @GetMapping("/getAllByBoardId")
    public AppResult<List<Article>> getAllByBoardId(@Parameter(description = "板块id") @RequestParam(value = "boardId",required = false)  Long boardId) {
        List<Article> articles;
        //查询所有列表
        if(boardId == null) {
             articles = articleService.selectAll();
        }else {
             articles = articleService.selectAllByBoardId(boardId);
        }
        if(articles == null) {
            articles = new ArrayList<>();
        }
        return AppResult.success(articles);
    }


    @Operation(summary = "根据Id查询帖⼦详情")
    @GetMapping("/details")
    public  AppResult<Article> getDetails(HttpServletRequest request,
                                          @Parameter(description = "帖子id") @RequestParam(value = "id") @NonNull Long id) {

        // 从session中获取当前登录的用户
        HttpSession session = request.getSession(false);
        User user =(User)session.getAttribute(AppConfig.USER_SESSION);

        // 调用Service，获取帖子详情
        Article article = articleService.selectDetailById(id);
        if(article == null) {
            return AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS);
        }
        // 判断当前用户是否为作者
        if(user.getId() == article.getUserId()) {
            article.setOwn(true);
        }
        return AppResult.success(article);
    }

    @Operation(summary = "更改帖子")
    @PostMapping("/modify")
    public AppResult modify(HttpServletRequest request,
                            @Parameter(description = "帖子id") @RequestParam("id") @NonNull Long id,
                            @Parameter(description = "帖子标题") @RequestParam("title") @NonNull String title,
                            @Parameter(description = "帖子正文") @RequestParam("content") @NonNull String content) {

        //从session中获取当前登录的用户
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);

        // 校验用户状态
        if(user.getState() == 1) {
            // 返回错误描述
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        // 查询帖子详情
        Article article = articleService.selectById(id);
        if (article == null) {
            // 返回帖子不存在
            return AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS);
        }
        // 判断帖子的状态 - 已归档
        if(article.getState() == 1 || article.getDeleteState() == 1) {
            return AppResult.failed(ResultCode.FAILED_ARTICLE_BANNED);
        }

        articleService.modify(id, title, content);
        log.info("帖子更新成功. Article id = " + id + "User id = " + user.getId() + ".");
        return AppResult.success();
    }
}
