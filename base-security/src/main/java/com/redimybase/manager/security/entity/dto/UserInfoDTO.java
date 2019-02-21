package com.redimybase.manager.security.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息详情Model
 * Created by Vim 2019/2/14 16:45
 *
 * @author Vim
 */
@Data
@ApiModel("用户信息详情Model")
public class UserInfoDTO implements Serializable {

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("性别")
    private Integer sex;

    @ApiModelProperty("生日")
    private String birthday;

    @ApiModelProperty("所属部门")
    private String orgName;

    @ApiModelProperty("所属职位")
    private String positionName;

    @ApiModelProperty("身份证")
    private String idNo;

    @ApiModelProperty("家庭住址")
    private String address;

    @ApiModelProperty("工位")
    private String station;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("手机号")
    private String phone;


    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("座机")
    private String telephone;
}

