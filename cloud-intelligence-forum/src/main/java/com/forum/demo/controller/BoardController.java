package com.forum.demo.controller;

import com.forum.demo.common.AppResult;
import com.forum.demo.common.ResultCode;
import com.forum.demo.exception.ApplicationException;
import com.forum.demo.model.Board;
import com.forum.demo.service.IBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Tag(name = "板块接口")
@RestController
@RequestMapping("/board")
public class BoardController {

   @Value("${cloud-intelligence-forum.index.board-num:9}")
    private Integer indexBoardNum;

    @Resource
    private IBoardService boardService;

    /**
     * 查询首版本列表
     * @return
     */
    @Operation(summary = "获取首页板块列表")
    @GetMapping("/topList")
    public AppResult<List<Board>> topList () {
        log.info("首页板块个数:"+indexBoardNum);
        //调用service查询结果
        List<Board> boards = boardService.selectByNum(indexBoardNum);

        if(boards == null) {
            boards = new ArrayList<>();
        }
        return AppResult.success(boards);
    }

    /**
     *
     * @param id
     * @return
     */
    @Operation(summary = "获取板块信息")
    @GetMapping("getById")
    public AppResult<Board> getById(@Parameter(description = "板块id") @RequestParam("id") @NonNull Long id) {
       //调用service
        Board board = boardService.selectById(id);
        if(board == null || board.getDeleteState() == 1) {
            log.warn(ResultCode.FAILED_BOARD_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }
        return AppResult.success(board);
    }
}
