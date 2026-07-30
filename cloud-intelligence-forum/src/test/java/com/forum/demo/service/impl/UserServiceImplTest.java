package com.forum.demo.service.impl;

import com.forum.demo.dao.UserMapper;
import com.forum.demo.model.User;
import com.forum.demo.service.IUserService;
import com.forum.demo.utils.Md5Utils;
import com.forum.demo.utils.UuidUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Resource
    private IUserService userService;
    @Test
    void createNormalUser() {
        User user = new User();
        user.setUsername("2925");
        user.setNickname("瑾桉");
        //定义原始密码
        String password = "123456";
        // 生成盐
        String salt = UuidUtil.UUID_32();
        // 生成密码的密文
        String mdPassword = Md5Utils.md5Salt(password, salt);
        // 设置加密后的密码
        user.setPassword(mdPassword);
        // 设置盐
        user.setSalt(salt);
        // 调用Service层的方法
        userService.createNormalUser(user);
        // 打印结果
        System.out.println(user);
    }
}