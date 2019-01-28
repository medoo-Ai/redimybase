package com.aispread.manager.flowable.service.impl;

import com.aispread.manager.flowable.dto.FlowDistributeRecord;
import com.aispread.manager.flowable.entity.FlowDistributeTaskEntity;
import com.aispread.manager.flowable.mapper.FlowDistributeTaskMapper;
import com.aispread.manager.flowable.service.FlowDistributeTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 流程分发任务表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-01-18
 */
@Service
public class FlowDistributeTaskServiceImpl extends ServiceImpl<FlowDistributeTaskMapper, FlowDistributeTaskEntity> implements FlowDistributeTaskService {

    @Override
    public List<FlowDistributeRecord> getRecord(String businessId, String taskCode) {
        return baseMapper.getRecord(businessId, taskCode);
    }
}
