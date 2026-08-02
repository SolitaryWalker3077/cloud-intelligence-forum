package com.forum.demo.service;

import com.forum.demo.model.Board;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.List;

public interface IBoardService {

     List<Board> selectByNum(Integer num);
}
