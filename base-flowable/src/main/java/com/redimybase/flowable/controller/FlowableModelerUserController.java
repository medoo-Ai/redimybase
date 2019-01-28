package com.redimybase.flowable.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.app.security.SecurityUtils;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 重定向Flowable用户登录地址
 * Created by Vim 2018/10/11 0011 0:24
 */
@RestController
@RequestMapping("flowable")
@Api(tags = "重定向Flowable用户登录地址")
public class FlowableModelerUserController {

    @RequestMapping("account")
    @ApiOperation(value = "Flowable流程设计器登录接口")
    public String account() {
        return "{\"id\":\"admin\",\"firstName\":\"Test\",\"lastName\":\"Administrator\",\"email\":\"admin@flowable.org\",\"fullName\":\"Test Administrator\",\"groups\":[],\"privileges\":[\"access-idm\",\"access-task\",\"access-modeler\",\"access-admin\"]}\n";
    }

}
