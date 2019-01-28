package com.aispread.manager.flowable.service.impl;

import com.aispread.manager.flowable.dto.FlowTodoTask;
import com.aispread.manager.flowable.dto.FlowTodoTaskQuery;
import com.aispread.manager.flowable.dto.TaskPage;
import com.aispread.manager.flowable.entity.ActRuTaskEntity;
import com.aispread.manager.flowable.mapper.RuTaskMapper;
import com.aispread.manager.flowable.service.ActRuTaskService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vim
 * @since 2018-10-11
 */
@Service
public class ActRuTaskServiceImpl extends ServiceImpl<RuTaskMapper, ActRuTaskEntity> implements ActRuTaskService {

    @Override
    public TaskPage<FlowTodoTask> taskList(TaskPage page, FlowTodoTaskQuery query) {
        if (StringUtils.isNotEmpty(query.getTitle())) {
            query.setTitle("%" + query.getTitle() + "%");
        }else{
            query.setTitle(null);
        }
        if (StringUtils.isNotEmpty(query.getNodeName())) {
            query.setNodeName("%" + query.getNodeName() + "%");
        }else{
            query.setNodeName(null);
        }
        if (StringUtils.isNotEmpty(query.getUserName())) {
            query.setUserName("%" + query.getUserName() + "%");
        }else{
            query.setUserName(null);
        }
        if (StringUtils.isEmpty(query.getStartTime())) {
            query.setStartTime(null);
        }
        if (StringUtils.isEmpty(query.getEndTime())) {
            query.setEndTime(null);
        }
        if (StringUtils.isEmpty(query.getUserId())) {
            query.setUserId(null);
        }
        if (StringUtils.isEmpty(query.getCategoryId())) {
            query.setCategoryId(null);
        }
        return baseMapper.taskList(page,query);
    }

}
