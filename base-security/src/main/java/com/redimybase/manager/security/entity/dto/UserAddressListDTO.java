package com.redimybase.manager.security.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 人员通讯录DTO
 * Created by Vim 2018/12/27 13:47
 *
 * @author Vim
 */
@Data
public class UserAddressListDTO implements Serializable {

    private String id;
    /**
     * 用戶名称
     */
    private String userName;

    /**
     * 头像链接
     */
    private String avatarUrl;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 电话
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 组织ID
     */
    private String orgId;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 办公室
     */
    private String office;

    /**
     * 工位
     */
    private String station;


}
