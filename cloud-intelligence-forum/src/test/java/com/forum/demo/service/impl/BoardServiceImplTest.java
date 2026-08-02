package com.forum.demo.service.impl;

import com.forum.demo.model.Board;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceImplTest {

    @Resource
    BoardServiceImpl boardService;

    @Test
    void selectByNum() {
        List<Board> boards = boardService.selectByNum(1);
        System.out.println(boards);
    }

    @Test
    void selectById() {
        Board board = boardService.selectById(1L);
        System.out.println(board);
    }

    @Transactional// 测试方法完成之后回滚数据库操作
    @Test
    void addOneArticleCountById() {
        boardService.addOneArticleCountById(1L);
        System.out.println("更新成功");
    }
}