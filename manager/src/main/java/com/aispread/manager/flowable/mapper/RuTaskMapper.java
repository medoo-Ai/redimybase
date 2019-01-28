package com.aispread.manager.flowable.mapper;

import com.aispread.manager.flowable.dto.FlowTodoTask;
import com.aispread.manager.flowable.dto.FlowTodoTaskQuery;
import com.aispread.manager.flowable.dto.TaskPage;
import com.aispread.manager.flowable.entity.ActRuTaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2018-10-11
 */
public interface RuTaskMapper extends BaseMapper<ActRuTaskEntity> {

    /**
     * 获取流程任务列表
     * @param query 查询条件
     */
    public TaskPage<FlowTodoTask> taskList(TaskPage page, @Param("query") FlowTodoTaskQuery query);
}
