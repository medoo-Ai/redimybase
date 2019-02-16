package com.redimybase.flowable.controller;

import com.aispread.manager.flowable.entity.FlowDefinitionEntity;
import com.aispread.manager.flowable.entity.FlowNodeEntity;
import com.aispread.manager.flowable.service.FlowDefinitionService;
import com.aispread.manager.flowable.service.FlowNodeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import com.aispread.manager.flowable.entity.FlowUserEntity;
import com.aispread.manager.flowable.mapper.FlowUserMapper;
import com.aispread.manager.flowable.service.impl.FlowUserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程用户controller
 * Created by Vim 2018/10/25 17:37
 */
@RestController
@RequestMapping("flow/user")
@Api(tags = "流程用户配置接口")
public class FlowUserController extends TableController<String, FlowUserEntity, FlowUserMapper, FlowUserServiceImpl> {


    /**
     * 根据节点ID获取审批用户信息
     */
    @PostMapping("queryByNodeId")
    @ApiOperation(value = "根据节点ID获取审批用户信息")
    public R<?> queryByNodeId(@ApiParam(value = "nodeId") String nodeId) {
        return new R<>(service.getFlowUserByNodeId(nodeId));
    }

    @Override
    public void beforeSave(FlowUserEntity entity) {
        FlowNodeEntity flowNodeEntity;
        if (StringUtils.isBlank(entity.getId())) {
            flowNodeEntity = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("id", entity.getNodeId()).select("id,definition_id"));

        } else {
            FlowUserEntity flowUserEntity = service.getOne(new QueryWrapper<FlowUserEntity>().eq("id", entity.getId()).select("id,node_id"));
            flowNodeEntity = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("id", flowUserEntity.getNodeId()).select("id,definition_id"));
        }
        FlowDefinitionEntity flowDefinitionEntity = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("id", flowNodeEntity.getDefinitionId()).select("id,status"));

        if (FlowDefinitionEntity.Status.已发布.equals(flowDefinitionEntity.getStatus())) {
            throw new BusinessException(R.失败, "请将流程作废再进行配置");
        }

        if (StringUtils.isBlank(entity.getId())) {
            //新增
            if (entity.getType() == FlowUserEntity.Type.普通用户 || entity.getType() == FlowUserEntity.Type.流程发起人 || entity.getType() == FlowUserEntity.Type.上级领导) {
                FlowUserEntity one = service.getOne(new QueryWrapper<FlowUserEntity>().eq("node_id", entity.getNodeId()).select("id"));
                if (null != one) {
                    service.removeById(one.getId());
                }
            }
        }
    }

    @Override
    public void beforeDelete(String id) {
        FlowUserEntity flowUserEntity = service.getOne(new QueryWrapper<FlowUserEntity>().eq("id", id).select("id,node_id"));
        FlowNodeEntity flowNodeEntity = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("id", flowUserEntity.getNodeId()).select("id,definition_id"));

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
    private FlowUserServiceImpl service;

    @Override
    protected FlowUserServiceImpl getService() {
        return service;
    }
}
