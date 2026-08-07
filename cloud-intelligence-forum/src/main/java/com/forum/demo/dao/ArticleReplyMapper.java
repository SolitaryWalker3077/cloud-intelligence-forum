package com.forum.demo.dao;

import com.forum.demo.model.ArticleReply;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleReplyMapper {
    int insert(ArticleReply record);

    int insertSelective(ArticleReply record);

    ArticleReply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArticleReply record);

    int updateByPrimaryKey(ArticleReply record);

    /**
     * 根据帖子Id查询所有的回复
     * @param articleId
     * @return
     */
    List<ArticleReply> selectByArticleId (@Param("articleId") Long articleId);
}