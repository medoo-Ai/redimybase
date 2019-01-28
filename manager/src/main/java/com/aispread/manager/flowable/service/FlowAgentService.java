package com.aispread.manager.flowable.service;

import com.aispread.manager.flowable.entity.FlowAgentEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 流程任务代理人表 服务类
 * </p>
 *
 * @author vim
 * @since 2019-01-21
 */
public interface FlowAgentService extends IService<FlowAgentEntity> {

    /**
     * 获取用户对应的代理人
     */
    public FlowAgentEntity findAgent(String userId, String flowDefinitionId);
}
