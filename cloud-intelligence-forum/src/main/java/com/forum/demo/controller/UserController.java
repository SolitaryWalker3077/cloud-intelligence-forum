package com.forum.demo.controller;

import com.forum.demo.common.AppResult;
import com.forum.demo.common.ResultCode;
import com.forum.demo.config.AppConfig;
import com.forum.demo.model.User;
import com.forum.demo.service.IUserService;
import com.forum.demo.utils.Md5Utils;
import com.forum.demo.utils.StringUtil;
import com.forum.demo.utils.UuidUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


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


    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     */
    @Operation(summary = "用户登录")
    @PostMapping("login")
    public AppResult login(HttpServletRequest request,
                      @Parameter(description = "用户名") String username,
                      @Parameter(description = "密码") String password) {

        User user = userService.login(username,password);
        // 如果登录成功把User对象设置到Session作用域中
        HttpSession session = request.getSession(true);
        session.setAttribute(AppConfig.USER_SESSION, user);
        return AppResult.success();
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("info")
    public AppResult<User> getUserInfo(HttpServletRequest request,
                                       @Parameter(description = "用户ID") @RequestParam(value = "id",required = false) Long id) {
        User user = null;
        //根据id判断User对象获取方式
        if(id == null) {
            //如果id为空,就从session当中获得User对象
            HttpSession session = request.getSession(false);
            //判断session获取的信息是否有效
            if(session == null || session.getAttribute(AppConfig.USER_SESSION) == null) {
                //用户未登录,session无效
                return AppResult.failed(ResultCode.FAILED_FORBIDDEN);
            }
            // 从session中获取当前登录的用户信息
            user = (User) session.getAttribute(AppConfig.USER_SESSION);
        }else {
            // 如果id不为空，从数据库中按Id查询出用户信息
             user = userService.selectById(id);
        }
        // 判断用户对象是否为空
        if(user == null) {
            return AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS);
        }
        return AppResult.success(user);
    }

    @Operation(summary = "用户退出")
    @GetMapping("logout")
    public AppResult logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        //判断session是否有效
        //不为空则代表没有退出
        if(session != null) {
            log.info("退出成功");
            session.invalidate();
        }
        return AppResult.success("退出成功");
    }
}
