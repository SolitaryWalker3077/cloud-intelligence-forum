package com.forum.demo.controller;

import ch.qos.logback.core.util.MD5Util;
import com.forum.demo.common.AppResult;
import com.forum.demo.common.ResultCode;
import com.forum.demo.model.User;
import com.forum.demo.service.IUserService;
import com.forum.demo.utils.Md5Utils;
import com.forum.demo.utils.StringUtil;
import com.forum.demo.utils.UuidUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "用户接口")
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private IUserService userService;

    /**
     * 用户注册
     * @param username 用户名
     * @param nickname 用户昵称
     * @param password 密码
     * @param passwordRepeat 重复密码
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public AppResult register (@Parameter(description = "用户名") @RequestParam("username") @NonNull String username,
                               @Parameter(description = "昵称") @RequestParam("nickname") @NonNull String nickname,
                               @Parameter(description = "密码") @RequestParam("password") @NonNull String password,
                               @Parameter(description = "确认密码") @RequestParam("passwordRepeat") @NonNull String passwordRepeat) {

        //非空校验
        if(StringUtil.isEmpty(username)
                || StringUtil.isEmpty(nickname)
                || StringUtil.isEmpty(password)
                || StringUtil.isEmpty(passwordRepeat)) {
            return AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE);
        }

        // 校验密码与重复密码是否相同
        if(!password.equals(passwordRepeat)) {
            log.warn(ResultCode.FAILED_TWO_PWD_NOT_SAME.toString());
            return AppResult.failed(ResultCode.FAILED_TWO_PWD_NOT_SAME);
        }

       // 准备数据
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);
        //处理密码
        //生成盐值
        String salt = UuidUtil.UUID_32();
        //给密码加密
        String encryptPassword = Md5Utils.md5Salt(password, salt);
        user.setPassword(encryptPassword);
        user.setSalt(salt);
        // 调用Service层
        userService.createNormalUser(user);
        // 返回成功
        return AppResult.success();
    }
}
