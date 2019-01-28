package com.aispread.manager.flowable.service;

import com.aispread.manager.flowable.dto.FlowDistributeRecord;
import com.aispread.manager.flowable.entity.FlowDistributeTaskEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流程分发任务表 服务类
 * </p>
 *
 * @author vim
 * @since 2019-01-18
 */
public interface FlowDistributeTaskService extends IService<FlowDistributeTaskEntity> {


    /**
     * 获取分发任务记录
     */
    public List<FlowDistributeRecord> getRecord(String businessId, String taskCode);
}
