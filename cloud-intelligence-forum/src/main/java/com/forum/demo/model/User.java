package com.forum.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    @Schema(description = "用户id")
    private Long id;
    @Schema(description = "用户名")
    private String username;

    @JsonIgnore
    private String password;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "电话号")
    private String phoneNum;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "性别")
    private Byte gender;

    //不参与序列化
    @JsonIgnore
    private String salt;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "文章数量")
    private Integer articleCount;

    @Schema(description = "是否为管理员")
    private Byte isAdmin;

    @Schema(description = "个人简介")
    private String remark;

    @Schema(description = "用户状态")
    private Byte state;

    @JsonIgnore
    private Byte deleteState;

    @Schema(description = "注册日期")
    private Date createTime;

    private Date updateTime;

}