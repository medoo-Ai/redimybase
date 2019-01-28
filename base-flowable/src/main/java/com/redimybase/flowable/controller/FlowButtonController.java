package com.redimybase.flowable.controller;

import com.aispread.manager.flowable.entity.FlowButtonEntity;
import com.aispread.manager.flowable.mapper.FlowButtonMapper;
import com.aispread.manager.flowable.service.impl.FlowButtonServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
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
        if (StringUtils.isBlank(entity.getId())) {
            //新增
            if (service.count(new QueryWrapper<FlowButtonEntity>().eq("type", entity.getType()).eq("node_id", entity.getNodeId())) > 0) {
                return R.fail("该操作按钮已存在,请尝试重新配置");
            }
            entity.setCreateTime(new Date());
        }
        return new R<>(super.save(entity));
    }

    @Autowired
    private FlowButtonServiceImpl service;

    @Override
    protected FlowButtonServiceImpl getService() {
        return service;
    }
}
