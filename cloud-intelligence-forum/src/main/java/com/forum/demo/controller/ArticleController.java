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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;

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
}
