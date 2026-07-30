package com.forum.demo;

import com.forum.demo.dao.UserMapper;
import com.forum.demo.model.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

@SpringBootTest
class CloudIntelligenceForumApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("hhh");
    }

    @Resource
    private DataSource dataSource;

    @Autowired
    private UserMapper userMapper;

    @Test
    void testConnection() throws SQLException {
        System.out.println("dataSource = " + dataSource.getClass());
        // 获取数据库连接
        Connection connection = dataSource.getConnection();
        System.out.println("connection = " + connection);
        connection.close();
    }

    @Test
    void testMybatis () {
        User user = userMapper.selectByPrimaryKey(1l);
        System.out.println(user.toString());
        System.out.println(user.getUsername());
    }

    @Test
    void testUUID () {
        System.out.println(UUID.randomUUID().toString());
    }
}
