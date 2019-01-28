package com.redimybase.manager.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redimybase.manager.security.entity.TaskSubEntity;
import com.redimybase.manager.security.entity.dto.TaskSubDTO;

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
