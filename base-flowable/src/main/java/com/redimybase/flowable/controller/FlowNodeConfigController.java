package com.redimybase.flowable.controller;

import com.aispread.manager.flowable.entity.FlowNodeConfigEntity;
import com.aispread.manager.flowable.mapper.FlowNodeConfigMapper;
import com.aispread.manager.flowable.service.impl.FlowNodeConfigServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.web.TableController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;

/**
 * 流程节点配置Controller
 * Created by Vim 2019/1/14 20:39
 *
 * @author Vim
 */
@RestController
@RequestMapping("flowable/node/config")
@Api(tags = "流程节点配置接口")
public class FlowNodeConfigController extends TableController<String, FlowNodeConfigEntity, FlowNodeConfigMapper, FlowNodeConfigServiceImpl> {


    @Override
    public void beforeSave(FlowNodeConfigEntity entity) {
        entity.setCreateTime(new Date());
        service.remove(new QueryWrapper<FlowNodeConfigEntity>().eq("node_id", entity.getNodeId()));
    }

    @Autowired
    private FlowNodeConfigServiceImpl service;

    @Override
    protected FlowNodeConfigServiceImpl getService() {
        return service;
    }
}
