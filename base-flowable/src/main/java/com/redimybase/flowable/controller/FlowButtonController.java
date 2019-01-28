package com.redimybase.flowable.controller;

import com.aispread.manager.flowable.entity.FlowButtonEntity;
import com.aispread.manager.flowable.entity.FlowDefinitionEntity;
import com.aispread.manager.flowable.entity.FlowNodeEntity;
import com.aispread.manager.flowable.entity.FlowVarEntity;
import com.aispread.manager.flowable.mapper.FlowButtonMapper;
import com.aispread.manager.flowable.service.FlowDefinitionService;
import com.aispread.manager.flowable.service.FlowNodeService;
import com.aispread.manager.flowable.service.impl.FlowButtonServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;

/**
 * 流程按钮Controller
 * Created by Vim 2019/1/9 10:34
 *
 * @author Vim
 */
@RestController
@RequestMapping("flow/button")
@Api(tags = "流程按钮配置接口")
public class FlowButtonController extends TableController<String, FlowButtonEntity, FlowButtonMapper, FlowButtonServiceImpl> {

    @Override
    public R<?> save(FlowButtonEntity entity) {
        FlowNodeEntity flowNodeEntity;
        if (StringUtils.isBlank(entity.getId())) {
            flowNodeEntity = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("id", entity.getNodeId()).select("id,definition_id"));

        } else {
            FlowButtonEntity flowButtonEntity = service.getOne(new QueryWrapper<FlowButtonEntity>().eq("id", entity.getId()).select("id,node_id"));
            flowNodeEntity = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("id", flowButtonEntity.getNodeId()).select("id,definition_id"));
        }
        FlowDefinitionEntity flowDefinitionEntity = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("id", flowNodeEntity.getDefinitionId()).select("id,status"));

        if (FlowDefinitionEntity.Status.已发布.equals(flowDefinitionEntity.getStatus())) {
            throw new BusinessException(R.失败, "请将流程作废再进行配置");
        }


        if (StringUtils.isBlank(entity.getId())) {
            //新增
            if (service.count(new QueryWrapper<FlowButtonEntity>().eq("type", entity.getType()).eq("node_id", entity.getNodeId())) > 0) {
                return R.fail("该操作按钮已存在,请尝试重新配置");
            }
            entity.setCreateTime(new Date());
        }
        return new R<>(super.save(entity));
    }

    @Override
    public void beforeDelete(String id) {
        FlowButtonEntity flowButtonEntity = service.getOne(new QueryWrapper<FlowButtonEntity>().eq("id", id).select("id,node_id"));
        FlowNodeEntity flowNodeEntity = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("id", flowButtonEntity.getNodeId()).select("id,definition_id"));

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
    private FlowButtonServiceImpl service;

    @Override
    protected FlowButtonServiceImpl getService() {
        return service;
    }
}
