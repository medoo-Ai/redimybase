package com.aispread.manager.flowable.service;

import com.aispread.manager.flowable.dto.FlowTodoTask;
import com.aispread.manager.flowable.dto.FlowTodoTaskQuery;
import com.aispread.manager.flowable.dto.TaskPage;
import com.aispread.manager.flowable.entity.ActRuTaskEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vim
 * @since 2018-10-11
 */
public interface ActRuTaskService extends IService<ActRuTaskEntity> {

    /**
     * 获取待办任务列表
     * @param query 查询条件
     */
    public TaskPage<FlowTodoTask> taskList(TaskPage page, FlowTodoTaskQuery query);
}
