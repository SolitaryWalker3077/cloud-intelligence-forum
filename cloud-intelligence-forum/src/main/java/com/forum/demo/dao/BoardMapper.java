package com.forum.demo.dao;

import com.forum.demo.model.Board;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    int insert(Board record);

    int insertSelective(Board record);

    Board selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Board record);

    int updateByPrimaryKey(Board record);

    List<Board> selectByNum(@Param("num") Integer num);
}