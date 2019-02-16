package com.aispread.manager.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author SyntacticSugar
 * @since 2019-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user")
public class User extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 登录账号
     */
    @TableField("account")
    private String account;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 性别(0:男,1:女)
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 头像(不建议外链)
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 身份证
     */
    @TableField("id_no")
    private String idNo;

    /**
     * 创建用户ID
     */
    @TableField("creator_id")
    private String creatorId;

    /**
     * 创建用户
     */
    @TableField("creator")
    private String creator;

    /**
     * 最后更新用户ID
     */
    @TableField("reviser_id")
    private String reviserId;

    /**
     * 最后更新用户
     */
    @TableField("reviser")
    private String reviser;

    /**
     * 状态(0:删除,1:启用,2:禁用)
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 电话
     */
    @TableField("telephone")
    private String telephone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
