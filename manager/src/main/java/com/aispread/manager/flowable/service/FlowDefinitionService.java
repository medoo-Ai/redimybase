package com.aispread.manager.flowable.service;

import com.aispread.manager.flowable.dto.FlowDefinitionListPage;
import com.aispread.manager.flowable.entity.FlowDefinitionEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流程定义表 服务类
 * </p>
 *
 * @author vim
 * @since 2018-10-24
 */
public interface FlowDefinitionService extends IService<FlowDefinitionEntity> {

    /**
     * 取流程模板列表
     */
    public FlowDefinitionListPage<FlowDefinitionEntity> list(FlowDefinitionListPage page);
}
