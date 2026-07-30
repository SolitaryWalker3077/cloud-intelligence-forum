package com.forum.demo.service.impl;

import com.forum.demo.common.AppResult;
import com.forum.demo.common.ResultCode;
import com.forum.demo.dao.UserMapper;
import com.forum.demo.exception.ApplicationException;
import com.forum.demo.model.User;
import com.forum.demo.service.IUserService;
import com.forum.demo.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Date;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NativeWebRequest nativeWebRequest;

    @Override
    public void createNormalUser(User user) {
        //非空校验
        if (user == null || StringUtil.isEmpty(user.getUsername())
                || StringUtil.isEmpty(user.getNickname()) || StringUtil.isEmpty(user.getPassword())
                || StringUtil.isEmpty(user.getSalt())) {
            //打印参数错误日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        //按用户名查询用户信息
        User userName = userMapper.selectByUserName(user.getUsername());
        if (userName != null) {
            log.warn(ResultCode.FAILED_USER_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 新增用户流程,设备默认值
        user.setGender((byte) 2);
        user.setArticleCount(0);
        user.setIsAdmin((byte) 0);
        user.setState((byte) 0);
        user.setDeleteState((byte) 0);

        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        //新增用户,写入数据库
        int row = userMapper.insertSelective(user);
        if(row != 1) {
            log.warn(ResultCode.FAILED_CREATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        log.info("新用户注册成功,username = "+ user.getUsername()+". ");
    }
}
