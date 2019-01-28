package com.aispread.manager.flowable.service.impl;

import com.aispread.manager.flowable.entity.FlowAgentEntity;
import com.aispread.manager.flowable.mapper.FlowAgentMapper;
import com.aispread.manager.flowable.service.FlowAgentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流程任务代理人表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-01-21
 */
@Service
public class FlowAgentServiceImpl extends ServiceImpl<FlowAgentMapper, FlowAgentEntity> implements FlowAgentService {

    @Override
    public FlowAgentEntity findAgent(String userId, String flowDefinitionId) {
        return baseMapper.fingByUserIdAndFlowDefinitionId(userId, flowDefinitionId);
    }
}
