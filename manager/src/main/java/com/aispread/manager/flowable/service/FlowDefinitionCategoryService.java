package com.aispread.manager.flowable.service;

import com.redimybase.framework.bean.R;
import com.aispread.manager.flowable.entity.FlowDefinitionCategoryEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 流程定义种类表 服务类
 * </p>
 *
 * @author vim
 * @since 2018-10-24
 */
public interface FlowDefinitionCategoryService extends IService<FlowDefinitionCategoryEntity> {

    /**
     * 获取流程定义种类树
     */
    public R<?> getCategoryTree(String name);
}
