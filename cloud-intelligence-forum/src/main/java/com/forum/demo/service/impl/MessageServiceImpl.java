package com.forum.demo.service.impl;

import com.forum.demo.common.AppResult;
import com.forum.demo.common.ResultCode;
import com.forum.demo.dao.MessageMapper;
import com.forum.demo.exception.ApplicationException;
import com.forum.demo.model.Message;
import com.forum.demo.model.User;
import com.forum.demo.service.IMessageService;
import com.forum.demo.service.IUserService;
import com.forum.demo.utils.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class MessageServiceImpl implements IMessageService {


    @Resource
    private IUserService userService;
    @Resource
    private MessageMapper messageMapper;

    @Override
    public void create(Message message) {
        //非空校验
        if (message == null || message.getPostUserId() == null || message.getReceiveUserId() == null
                || StringUtil.isEmpty(message.getContent())) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 校验接收者是否存在
        User user = userService.selectById(message.getReceiveUserId());
        if(user == null || user.getDeleteState() == 1 || user.getState() == 1) {
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 设置默认值
        message.setState((byte)0);
        message.setDeleteState((byte)0);
        // 设置创建与更新时间
        Date date = new Date();
        message.setCreateTime(date);
        message.setUpdateTime(date);
        //调用dao
        int row = messageMapper.insertSelective(message);
        if(row != 1) {
            log.warn(ResultCode.FAILED_CREATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
    }
}
