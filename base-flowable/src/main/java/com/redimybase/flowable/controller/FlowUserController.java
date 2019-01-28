package com.redimybase.flowable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
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
        if (StringUtils.isBlank(entity.getId())) {
            //新增
            if (entity.getType() == FlowUserEntity.Type.USER || entity.getType() == FlowUserEntity.Type.INITIATOR || entity.getType() == FlowUserEntity.Type.LEADERSHIP) {
                FlowUserEntity one = service.getOne(new QueryWrapper<FlowUserEntity>().eq("node_id", entity.getNodeId()).select("id"));
                if (null != one) {
                    service.removeById(one.getId());
                }
            }
        }
    }

    @Autowired
    private FlowUserServiceImpl service;

    @Override
    protected FlowUserServiceImpl getService() {
        return service;
    }
}
