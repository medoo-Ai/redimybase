package com.aispread.manager.flowable.mapper;

import com.aispread.manager.flowable.dto.*;
import com.aispread.manager.flowable.entity.ActHiTaskinstEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2019-01-10
 */
public interface ActHiTaskinstMapper extends BaseMapper<ActHiTaskinstEntity> {
    public TaskPage taskList(TaskPage page ,@Param("query") FlowDoneTaskQuery query);

    public TaskPage applyTaskList(TaskPage<FlowApplyTask> page, @Param("query") FlowApplyTaskQuery query);

    public List<FlowApprovalRecord> approvalRecord(String processInstanceId);
}
