package com.forum.demo.service;

import com.forum.demo.model.Message;

public interface IMessageService {

    /**
     * 发送站内信息
     * @param message 站内信对象
     */
    void create(Message message);
}
