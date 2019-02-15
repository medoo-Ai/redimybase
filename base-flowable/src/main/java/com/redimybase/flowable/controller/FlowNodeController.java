package com.redimybase.flowable.controller;

import com.redimybase.framework.bean.R;
import com.redimybase.framework.web.TableController;
import com.aispread.manager.flowable.entity.FlowNodeEntity;
import com.aispread.manager.flowable.mapper.FlowNodeMapper;
import com.aispread.manager.flowable.service.impl.FlowNodeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程节点Controller
 * Created by Vim 2018/10/25 15:52
 */
@RestController
@RequestMapping("flow/node")
@Api(tags = "流程节点配置接口")
public class FlowNodeController extends TableController<String, FlowNodeEntity, FlowNodeMapper, FlowNodeServiceImpl> {


    @Override
    public void beforeSave(FlowNodeEntity entity) {
        if (StringUtils.isBlank(entity.getId())) {
            //新增
            if (null==entity.getInitiatorView()) {
                entity.setInitiatorView(1);
            }
            if (null == entity.getAssigneeView()) {
                entity.setAssigneeView(1);
            }
        }
    }

    @PostMapping("queryByDefinitionId")
    @ApiOperation(value = "根据ID获取指定类型的流程节点")
    public R<?> queryByDefinitionId(@ApiParam(value = "流程模板ID") String definitionId, @ApiParam(value = "流程节点类型") String type) {
        return service.queryByDefinitionId(definitionId,type);
    }





    @Autowired
    private FlowNodeServiceImpl service;

    @Override
    protected FlowNodeServiceImpl getService() {
        return service;
    }
}
