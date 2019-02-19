package com.aispread.manager.jointtask.service;

import com.aispread.manager.jointtask.entity.TaskMainEntity;
import com.aispread.manager.jointtask.entity.TaskSubEntity;
import com.redimybase.framework.exception.BusinessException;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 协同任务 服务类
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-10
 */
public interface TaskMainService extends IService<TaskMainEntity> {
  public boolean release(String id) throws BusinessException;

  public boolean complete(String id) throws BusinessException;

  public boolean updateSubTask(TaskSubEntity entity) throws BusinessException;

  boolean isRead(String id);
}
