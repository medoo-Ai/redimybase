package com.redimybase.flowable.controller;

import com.aispread.manager.flowable.entity.FlowDefinitionEntity;
import com.aispread.manager.flowable.entity.FlowNodeEntity;
import com.aispread.manager.flowable.entity.FlowUserEntity;
import com.aispread.manager.flowable.service.FlowDefinitionService;
import com.aispread.manager.flowable.service.FlowNodeService;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import com.aispread.manager.flowable.entity.FlowVarEntity;
import com.aispread.manager.flowable.mapper.FlowVarMapper;
import com.aispread.manager.flowable.service.impl.FlowVarServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 流程变量controller
 * Created by Vim 2018/10/25 17:04
 */
@RestController
@RequestMapping("flow/var")
@Api(tags = "流程变量配置接口")
public class FlowVarController extends TableController<String, FlowVarEntity, FlowVarMapper, FlowVarServiceImpl> {


    /**
     * 批量增加
     */
    @PostMapping("saveBatch")
    @ApiOperation(value = "批量增加流程变量")
    public R<?> saveBatch(String json) {
        List<FlowVarEntity> flowVarEntities = JSONArray.parseArray(json, FlowVarEntity.class);

        for (FlowVarEntity flowVarEntity : flowVarEntities) {
            super.save(flowVarEntity);
        }
        return new R<>(flowVarEntities);
    }


    @Override
    public void beforeDelete(String id) {
        FlowVarEntity flowVarEntity = service.getOne(new QueryWrapper<FlowVarEntity>().eq("id", id).select("id,node_id"));
        FlowNodeEntity flowNodeEntity = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("id", flowVarEntity.getNodeId()).select("id,definition_id"));

        FlowDefinitionEntity flowDefinitionEntity = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("id", flowNodeEntity.getDefinitionId()).select("id,status"));

        if (FlowDefinitionEntity.Status.已发布.equals(flowDefinitionEntity.getStatus())) {
            throw new BusinessException(R.失败, "请将流程作废再进行配置");
        }
    }

    @Override
    public void beforeSave(FlowVarEntity entity) {
        FlowNodeEntity flowNodeEntity;
        if (StringUtils.isBlank(entity.getId())) {
            flowNodeEntity = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("id", entity.getNodeId()).select("id,definition_id"));

        } else {
            FlowVarEntity flowVarEntity = service.getOne(new QueryWrapper<FlowVarEntity>().eq("id", entity.getId()).select("id,node_id"));
            flowNodeEntity = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("id", flowVarEntity.getNodeId()).select("id,definition_id"));
        }
        FlowDefinitionEntity flowDefinitionEntity = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("id", flowNodeEntity.getDefinitionId()).select("id,status"));

        if (FlowDefinitionEntity.Status.已发布.equals(flowDefinitionEntity.getStatus())) {
            throw new BusinessException(R.失败, "请将流程作废再进行配置");
        }
    }

    @Autowired
    private FlowNodeService flowNodeService;

    @Autowired
    private FlowDefinitionService flowDefinitionService;


    @Autowired
    private FlowVarServiceImpl service;

    @Override
    protected FlowVarServiceImpl getService() {
        return service;
    }
}
