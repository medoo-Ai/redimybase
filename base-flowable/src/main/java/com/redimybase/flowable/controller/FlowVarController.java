package com.redimybase.flowable.controller;

import com.alibaba.fastjson.JSONArray;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.web.TableController;
import com.aispread.manager.flowable.entity.FlowVarEntity;
import com.aispread.manager.flowable.mapper.FlowVarMapper;
import com.aispread.manager.flowable.service.impl.FlowVarServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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


    @Autowired
    private FlowVarServiceImpl service;

    @Override
    protected FlowVarServiceImpl getService() {
        return service;
    }
}
