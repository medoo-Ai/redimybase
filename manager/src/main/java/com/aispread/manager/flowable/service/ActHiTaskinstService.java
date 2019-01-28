package com.aispread.manager.flowable.service;

import com.aispread.manager.flowable.dto.*;
import com.aispread.manager.flowable.entity.ActHiTaskinstEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vim
 * @since 2019-01-10
 */
public interface ActHiTaskinstService extends IService<ActHiTaskinstEntity> {


    /**
     * 获取已办列表
     */
    public TaskPage<FlowDoneTask> taskList(TaskPage page, FlowDoneTaskQuery query);


    /**
     * 获取已申请列表
     */
    public TaskPage<FlowApplyTask> applyTaskList(TaskPage page, FlowApplyTaskQuery query);

    /**
     * 获取审批记录
     */
    public List<FlowApprovalRecord> approvalRecord(String processInstanceId);

}
