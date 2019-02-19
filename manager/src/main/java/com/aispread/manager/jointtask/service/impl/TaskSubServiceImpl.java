package com.aispread.manager.jointtask.service.impl;

import com.aispread.manager.jointtask.entity.TaskMainEntity;
import com.aispread.manager.jointtask.entity.TaskReadStatus;
import com.aispread.manager.jointtask.entity.TaskSubDTO;
import com.aispread.manager.jointtask.entity.TaskSubEntity;
import com.aispread.manager.jointtask.mapper.TaskSubMapper;
import com.aispread.manager.jointtask.service.TaskSubService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.exception.BusinessException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.security.utils.SecurityUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协同任务-子任务 服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-01-11
 */
@Service
public class TaskSubServiceImpl extends ServiceImpl<TaskSubMapper, TaskSubEntity> implements
    TaskSubService {
  @Autowired
  TaskReadStatusServiceImpl taskReadStatusService;
  @Autowired
  TaskMainServiceImpl mainService;

  @Override
  public TaskSubDTO getDto(String id) {
    return baseMapper.getDto(id);
  }

  /**
   * 修改子任务,子任务的[任务完成描述]发生变更时修改当前子任务的已读状态,并更新顶级任务的更新时间和更新人
   * @param entity
   * @return
   */
  @Override
  public boolean updateById(TaskSubEntity entity) {
    entity.setUpdateTime(new Date());
    entity.setReviserId(SecurityUtil.getCurrentUserId());
    QueryWrapper<TaskReadStatus> queryWrapper = new QueryWrapper<>();
    queryWrapper.and(i -> i.eq("tasksub_id",entity.getId()));
    /*当任务的完成描述发生变动时除当前操作者以外全部未读*/
    if(entity.getId() != null && StringUtils.isNotBlank(entity.getCompleteDesc()) && !getById(entity.getId()).getCompleteDesc().equals(entity.getCompleteDesc())){
      List<TaskReadStatus> readList = taskReadStatusService.list(queryWrapper);
      if(readList.size() == 0){
        /*如果没有当前任务的已读记录则先创建当前修改人已读的记录*/
        TaskReadStatus readStatus = new TaskReadStatus();
        readStatus.setTasksubId(entity.getId());
        readStatus.setReadStatu(true);
        readStatus.setUserId(SecurityUtil.getCurrentUserId());
        taskReadStatusService.save(readStatus);
      }else{
        /*否则修改所有当前任务的已读记录,除了修改人以外的已读记录全部改为未读*/
        for(TaskReadStatus r:readList){
          if(r.getUserId().equals(SecurityUtil.getCurrentUserId())){
            r.setReadStatu(true);
          }else{
            r.setReadStatu(false);
          }
          taskReadStatusService.updateById(r);
        }
      }
      /*修改顶级任务的最后更新时间和更新人*/
      updateParentUpdateTime(entity.getId());
    }
    return super.updateById(entity);
  }

  /**
   * 递归方法,更新顶级任务状态的更新时间和更新人
   * id 子任务ID
   * @param
   * @return
   */
  private void updateParentUpdateTime(String id) throws BusinessException{
    ArrayList<Integer> status = new ArrayList<Integer>();
    TaskSubEntity taskSubEntity  = null;
    /*判断父级类型*/
    boolean isMainTask = true;
    String parentId = "";
    try {
      parentId = getById(id).getParent();
      taskSubEntity = getById(parentId);
      isMainTask =  taskSubEntity != null?false:true;
    } catch (Exception e) {
      e.printStackTrace();
    }
      if(StringUtils.isNotBlank(parentId) && isMainTask){
        /*判定为顶级任务*/
        TaskMainEntity main = mainService.getById(parentId);
        mainService.save(main);
      }else{
        /*接着找上一级任务*/
        updateParentUpdateTime(taskSubEntity.getId());
      }
  }

  @Override
  public boolean removeById(Serializable id) {
    TaskSubEntity byId = getById(id);
    byId.setStatus(TaskSubEntity.Status.删除);
    return updateById(byId);
  }

  @Override
  public boolean removeByIds(Collection<? extends Serializable> idList) {
    idList.stream().forEach(id -> {
      TaskSubEntity byId = getById(id);
      byId.setStatus(0);
      updateById(byId);
    });
    return true;
  }

  public List<TaskSubDTO> list(TaskSubDTO query){
    return baseMapper.getList(query);
  }

  @Override
  public boolean save(TaskSubEntity entity) {
    if(entity != null && entity.getCreateTime() == null){
      entity.setCreateTime(new Date());
      entity.setCreatorId(SecurityUtil.getCurrentUserId());
    }
    super.save(entity);
    /*设置当前登陆人已读*/
    TaskReadStatus readStatus = new TaskReadStatus();
    readStatus.setTasksubId(entity.getId());
    readStatus.setReadStatu(true);
    readStatus.setUserId(SecurityUtil.getCurrentUserId());
    taskReadStatusService.save(readStatus);
    /*更新顶级任务的更新时间和更新人*/
    updateParentUpdateTime(entity.getId());
    return true;
  }
}
