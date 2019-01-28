package com.redimybase.manager.security.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户列表请求
 * Created by Vim 2019/1/14 11:24
 *
 * @author Vim
 */
@Data
@ApiModel(value = "用户列表Page")
public class UserListPage<T> extends Page<T> {

    @ApiModelProperty(value = "组织ID")
    private String orgId;
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户姓名")
    private String userName;
}
