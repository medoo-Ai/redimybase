package com.aispread.manager.jointtask.service;

import com.aispread.manager.jointtask.entity.TaskSubDTO;
import com.aispread.manager.jointtask.entity.TaskSubEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 协同任务-子任务 服务类
 * </p>
 *
 * @author vim
 * @since 2019-01-11
 */
public interface TaskSubService extends IService<TaskSubEntity> {
  public TaskSubDTO getDto(String id);
}
