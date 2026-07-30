package com.forum.demo.controller;

import com.forum.demo.exception.ApplicationException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/*
*   测试文档
* */

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping("t1")
    public String test() {
        /*
        * 测试api
        * */
        return "hhh";
    }

    @GetMapping("/exception")
    public String testException() throws Exception {
        throw new Exception("这是⼀个Exception");
    }
    @GetMapping("/appException")
    public String testApplicationException() {
        throw new ApplicationException("这是⼀个⾃定义的ApplicationException");
    }
}
