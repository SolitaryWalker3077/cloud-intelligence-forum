package com.forum.demo.service;

import com.forum.demo.model.User;

public interface IUserService {
    /**
     * 创建一个普通用户
     *
     * @param user 用户信息
     */
    void createNormalUser (User user);
}
