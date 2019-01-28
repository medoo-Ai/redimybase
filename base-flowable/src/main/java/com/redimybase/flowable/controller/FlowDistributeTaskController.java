package com.redimybase.flowable.controller;

import com.aispread.manager.flowable.entity.FlowDistributeTaskEntity;
import com.aispread.manager.flowable.mapper.FlowDistributeTaskMapper;
import com.aispread.manager.flowable.service.impl.FlowDistributeTaskServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.web.TableController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程分发任务Controller
 * Created by Vim 2019/1/18 13:54
 *
 * @author Vim
 */
@RestController
@RequestMapping("flowable/distribute")
@Api(tags = "流程分发任务接口")
public class FlowDistributeTaskController extends TableController<String, FlowDistributeTaskEntity, FlowDistributeTaskMapper, FlowDistributeTaskServiceImpl> {


    /**
     * 获取任务详情
     */
    @PostMapping("detailOne")
    public R<?> detail(String businessId, String taskCode) {
        return new R<>(service.getOne(
                new QueryWrapper<FlowDistributeTaskEntity>()
                        .eq("business_id", businessId)
                        .eq("task_code", taskCode)
        ));
    }


    @Autowired
    private FlowDistributeTaskServiceImpl service;

    @Override
    protected FlowDistributeTaskServiceImpl getService() {
        return service;
    }
}
