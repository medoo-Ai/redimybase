package com.redimybase.manager.security.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.manager.security.entity.TaskMainEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.redimybase.manager.security.entity.TaskSubEntity;
import com.redimybase.manager.security.entity.dto.TaskMainDTO;

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
