package com.redimybase.flowable.controller;

import com.aispread.manager.flowable.entity.FlowAgentEntity;
import com.aispread.manager.flowable.mapper.FlowAgentMapper;
import com.aispread.manager.flowable.service.impl.FlowAgentServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;

/**
 * 流程代理人配置
 * Created by Vim 2019/1/21 11:12
 *
 * @author Vim
 */
@RestController
@RequestMapping("flowable/agent")
@Api(tags = "流程代理人配置接口")
public class FlowAgentController extends TableController<String, FlowAgentEntity, FlowAgentMapper, FlowAgentServiceImpl> {


    @Override
    public void beforeSave(FlowAgentEntity entity) {
        String userId = SecurityUtil.getCurrentUserId();
        if (StringUtils.isBlank(entity.getDefinitionId())) {
            throw new BusinessException(201, "请选择需要代理的流程");
        }
        if (StringUtils.isBlank(entity.getAgentId())) {
            throw new BusinessException(201, "请选择代理人");
        }

        if (service.count(new QueryWrapper<FlowAgentEntity>().eq("definition_id", entity.getDefinitionId())) > 0 && entity.getContinueSave() == 0) {
            throw new BusinessException(201, "该流程已配置过代理人,继续保存将覆盖已配置的代理人,是否继续?");
        }
        entity.setUserId(userId);
        entity.setCreateTime(new Date());
    }


    @Autowired
    private FlowAgentServiceImpl service;

    @Override
    protected FlowAgentServiceImpl getService() {
        return service;
    }
}
