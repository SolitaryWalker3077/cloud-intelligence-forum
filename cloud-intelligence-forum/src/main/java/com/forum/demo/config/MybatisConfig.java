package com.forum.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.forum.demo.dao")
public class MybatisConfig {
}
