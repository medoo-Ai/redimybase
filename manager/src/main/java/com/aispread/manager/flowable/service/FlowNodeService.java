package com.aispread.manager.flowable.service;

import com.aispread.manager.flowable.entity.FlowNodeEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.redimybase.framework.bean.R;

/**
 * <p>
 * 流程节点表 服务类
 * </p>
 *
 * @author vim
 * @since 2018-10-24
 */
public interface FlowNodeService extends IService<FlowNodeEntity> {

    /**
     * 获取节点树
     */
    public R<?> queryByDefinitionId(String definitionId,String type);

}
