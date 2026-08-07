package com.forum.demo.service.impl;

import com.forum.demo.common.AppResult;
import com.forum.demo.common.ResultCode;
import com.forum.demo.dao.UserMapper;
import com.forum.demo.exception.ApplicationException;
import com.forum.demo.model.User;
import com.forum.demo.service.IUserService;
import com.forum.demo.utils.Md5Utils;
import com.forum.demo.utils.StringUtil;
import jakarta.annotation.Resource;
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
        if (row != 1) {
            log.warn(ResultCode.FAILED_CREATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        log.info("新用户注册成功,username = " + user.getUsername() + ". ");
    }

    @Override
    public User selectByUserName(String username) {
        //非空校验
        if (StringUtil.isEmpty(username)) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        return userMapper.selectByUserName(username);
    }

    @Override
    public User login(String username, String password) {
        //非空校验
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        //查询用户名
        User user = selectByUserName(username);
        if (user == null) {
            log.warn(ResultCode.FAILED_LOGIN.toString() + ",username:" + username);
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }
        //  对密码做校验
        String encryptPassword = Md5Utils.md5Salt(password, user.getSalt());
        if (!encryptPassword.equalsIgnoreCase(user.getPassword())) {
            log.warn(ResultCode.FAILED_LOGIN.toString() + "密码错误");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }
        log.info(username + ",登录成功");
        return user;
    }


    @Override
    public User selectById(Long id) {
        //非空判断
        if (id == null) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 调用DAO查询数据库并获取对象
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }


    @Override
    public void addOneArticleCountById(Long id) {
        if (id == null || id <= 0) {
            log.warn(ResultCode.FAILED_BOARD_ARTICLE_COUNT.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT));
        }

        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            // 打印日志
            log.warn(ResultCode.ERROR_IS_NULL.toString() + ", user id = " + id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        //更新用户发帖数量
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setArticleCount(user.getArticleCount() + 1);
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if (row != 1) {
            log.warn(ResultCode.FAILED.toString() + ", 受影响的行数不等于 1 .");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }

    @Override
    public void subOneArticleCountById(Long id) {
        if (id == null || id <= 0) {
            log.warn(ResultCode.FAILED_BOARD_ARTICLE_COUNT.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT));
        }
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            // 打印日志
            log.warn(ResultCode.ERROR_IS_NULL.toString() + ", user id = " + id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        //更新用户发帖数量
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setArticleCount(user.getArticleCount() - 1);
        if (updateUser.getArticleCount() < 0) {
            updateUser.setArticleCount(0);
        }
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if (row != 1) {
            log.warn(ResultCode.FAILED.toString() + ", 受影响的行数不等于 1 .");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }


    @Override
    public void modifyInfo(User user) {
        //非空校验
        if (user == null || user.getId() == null || user.getId() <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 校验用户是否存在
        User existsUser = userMapper.selectByPrimaryKey(user.getId());
        if(existsUser == null) {
            // 打印日志
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }
        //定义标志位
        boolean checkAttr = false; // false表示没有校验通过
        //定义一个专门用来更新的对象，防止用户传入的User对象设置了其他的属性
        //当使用动态SQL进行更新的时候，覆盖了没有经过校验的字段
        User updateUser = new User();
        //设置用户id
        updateUser.setId(user.getId());

        //对每个一个参数进行校验并赋值
        if(!StringUtil.isEmpty(user.getUsername())
        && !user.getUsername().equals(existsUser.getUsername())) {
            // 需要更新用户名(登录名)时，进行唯一性的校验
            User checkUser = userMapper.selectByUserName(user.getUsername());
            if (checkUser != null) {
                // 用户已存在
                log.warn(ResultCode.FAILED_USER_EXISTS.toString());
                // 抛出异常
                throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_EXISTS));
            }
            // 数据库中没有找到相应的用户，表示可以修改用户名
            updateUser.setUsername(user.getUsername());
            // 更新标志位
            checkAttr = true;
        }

        // 校验昵称
        if (!StringUtil.isEmpty(user.getNickname())
                && !user.getNickname().equals(existsUser.getNickname())) {
            // 设置昵称
            updateUser.setNickname(user.getNickname());
            // 更新标志位
            checkAttr = true;
        }
        //  校验性别
        if (user.getGender() != null && user.getGender() != existsUser.getGender()) {
            // 设置性别
            updateUser.setGender(user.getGender());
            // 合法性校验
            if (updateUser.getGender() > 2 || updateUser.getGender() < 0) {
                updateUser.setGender((byte) 2);
            }
            // 更新标志位
            checkAttr = true;
        }
        //  校验邮箱
        if (!StringUtil.isEmpty(user.getEmail())
                && !user.getEmail().equals(existsUser.getEmail())) {
            // 设置邮箱
            updateUser.setEmail(user.getEmail());
            // 更新标志位
            checkAttr = true;
        }
        // 校验电话号码
        if (!StringUtil.isEmpty(user.getPhoneNum())
                && !user.getPhoneNum().equals(existsUser.getPhoneNum())) {
            // 设置电话号码
            updateUser.setPhoneNum(user.getPhoneNum());
            // 更新标志位
            checkAttr = true;
        }
        //  校验个人简介
        if (!StringUtil.isEmpty(user.getRemark())
                && !user.getRemark().equals(existsUser.getRemark())) {
            // 设置电话号码
            updateUser.setRemark(user.getRemark());
            // 更新标志位
            checkAttr = true;
        }
        //  根据标志位来决定是否可以执行更新
        if (checkAttr == false) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        //  调用DAO
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if (row != 1) {
            log.warn(ResultCode.FAILED.toString() + ", 受影响的行数不等于 1 .");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }
}
