package com.aispread.manager.flowable.service.impl;

import com.aispread.manager.flowable.dto.*;
import com.aispread.manager.flowable.entity.ActHiTaskinstEntity;
import com.aispread.manager.flowable.entity.ActRuTaskEntity;
import com.aispread.manager.flowable.mapper.ActHiTaskinstMapper;
import com.aispread.manager.flowable.service.ActHiTaskinstService;
import com.aispread.manager.flowable.service.ActRuTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-01-10
 */
@Service
public class ActHiTaskinstServiceImpl extends ServiceImpl<ActHiTaskinstMapper, ActHiTaskinstEntity> implements ActHiTaskinstService {

    @Override
    public TaskPage<FlowDoneTask> taskList(TaskPage page, FlowDoneTaskQuery query) {
        if (StringUtils.isNotEmpty(query.getTitle())) {
            query.setTitle("%" + query.getTitle() + "%");
        } else {
            query.setTitle(null);
        }
        if (StringUtils.isEmpty(query.getUserId())) {
            query.setUserId(null);
        }
        if (StringUtils.isEmpty(query.getApplyStartTime())) {
            query.setApplyStartTime(null);
        }
        if (StringUtils.isEmpty(query.getApplyEndTime())) {
            query.setApplyEndTime(null);
        }
        if (StringUtils.isEmpty(query.getApprovalStartTime())) {
            query.setApprovalStartTime(null);
        }
        if (StringUtils.isEmpty(query.getApprovalEndTime())) {
            query.setApprovalEndTime(null);
        }
        if (StringUtils.isEmpty(query.getCategoryId())) {
            query.setCategoryId(null);
        }

        return baseMapper.taskList(page, query);
    }

    @Override
    public TaskPage<FlowApplyTask> applyTaskList(TaskPage page, FlowApplyTaskQuery query) {
        if (StringUtils.isNotEmpty(query.getTitle())) {
            query.setTitle("%" + query.getTitle() + "%");
        } else {
            query.setTitle(null);
        }
        if (StringUtils.isEmpty(query.getUserId())) {
            query.setUserId(null);
        }
        if (StringUtils.isEmpty(query.getStatus())) {
            query.setStatus(null);
        }
        if (StringUtils.isEmpty(query.getStartTime())) {
            query.setStartTime(null);
        }
        if (StringUtils.isEmpty(query.getEndTime())) {
            query.setEndTime(null);
        }
        if (StringUtils.isEmpty(query.getCategoryId())) {
            query.setCategoryId(null);
        }
        return baseMapper.applyTaskList(page, query);
    }

    @Override
    public List<FlowApprovalRecord> approvalRecord(String processInstanceId) {
        return baseMapper.approvalRecord(processInstanceId);
    }

    @Autowired
    private ActRuTaskService actRuTaskService;
}
