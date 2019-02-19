package com.aispread.manager.jointtask.mapper;

import com.aispread.manager.jointtask.entity.TaskSubDTO;
import com.aispread.manager.jointtask.entity.TaskSubEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

/**
 * <p>
 * 协同任务-子任务 Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2019-01-11
 */
public interface TaskSubMapper extends BaseMapper<TaskSubEntity> {
  public List<TaskSubDTO> getList(TaskSubDTO entity);
  public TaskSubDTO getDto(String id);
}
