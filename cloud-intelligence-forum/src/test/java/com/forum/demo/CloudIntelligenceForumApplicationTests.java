package com.forum.demo;

import com.forum.demo.dao.UserMapper;
import com.forum.demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CloudIntelligenceForumApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("hhh");
    }

    @Autowired
    private UserMapper userMapper;

    @Test
    void testMybatis () {
        User user = userMapper.selectByPrimaryKey(1l);
        System.out.println(user.toString());
        System.out.println(user.getUsername());
    }

}
