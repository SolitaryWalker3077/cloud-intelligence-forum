package com.forum.demo.service.impl;


import com.forum.demo.common.AppResult;
import com.forum.demo.common.ResultCode;
import com.forum.demo.dao.ArticleMapper;
import com.forum.demo.exception.ApplicationException;
import com.forum.demo.model.Article;
import com.forum.demo.model.Board;
import com.forum.demo.model.User;
import com.forum.demo.service.IArticleService;
import com.forum.demo.service.IBoardService;
import com.forum.demo.service.IUserService;
import com.forum.demo.utils.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ArticleServiceImpl implements IArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private IUserService userService;
    @Resource
    private IBoardService boardService;

    @Override
    public void create(Article article) {
        //非空校验
        if(article == null || article.getBoardId() == null || article.getUserId() == null
            || StringUtil.isEmpty(article.getTitle())
            || StringUtil.isEmpty(article.getContent())) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 设置默认值
        article.setVisitCount(0); // 访问数
        article.setReplyCount(0); // 回复数
        article.setLikeCount(0);  // 点赞数
        article.setDeleteState((byte) 0);
        article.setState((byte) 0);
        Date date = new Date();
        article.setCreateTime(date);
        article.setUpdateTime(date);
        // 写入数据库
        int articleRow = articleMapper.insertSelective(article);
        if(articleRow<=0) {
            log.warn(ResultCode.FAILED_CREATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }

        // 获取用户信息
        User user = userService.selectById(article.getUserId());
        //没有找到指定用户信息
        if(user == null) {
            log.warn(ResultCode.FAILED_CREATE.toString() + ", 发贴失败, user id = " + article.getUserId());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        // 更新用户的发贴数
        userService.addOneArticleCountById(user.getId());

        Board board = boardService.selectById(article.getBoardId());
        //没有找到指定板块
        if(board == null) {
            log.warn(ResultCode.FAILED_CREATE.toString() + ", 发贴失败, board id = " + article.getBoardId());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        //更新
        boardService.addOneArticleCountById(article.getBoardId());

        // 打印日志
        log.info(ResultCode.SUCCESS.toString() + ", user id = " + article.getUserId()
                + ", board id = " + article.getBoardId() + ", article id = "+article.getId() + "发帖成功");
    }


    @Override
    public List<Article> selectAll() {
        List<Article> articles = articleMapper.selectAll();
        return articles;
    }

    @Override
    public List<Article> selectAllByBoardId(Long boardId) {
        //非空校验
        if(boardId == null || boardId <= 0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        //查询板块是否存在
        if(boardId.equals(boardService.selectById(boardId))) {
            log.warn(ResultCode.FAILED_BOARD_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }
        //调用dao
        List<Article> articles = articleMapper.selectAllByBoardId(boardId);
        return articles;
    }

    @Override
    public Article selectDetailById(Long id) {
        if(id == null || id <= 0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        //调用dao
        Article article = articleMapper.selectDetailById(id);
        if(article == null) {
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS));
        }
        // 更新帖子的访问次数
        Article updateArticle = new Article();
        updateArticle.setId(article.getId());
        updateArticle.setVisitCount(article.getVisitCount()+1);
        // 保存到数据库
        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if(row != 1) {
            log.warn(ResultCode.ERROR_SERVICES.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
        // 更新返回对象的访问次数
        article.setVisitCount(article.getVisitCount()+1);
        return article;
    }
}
