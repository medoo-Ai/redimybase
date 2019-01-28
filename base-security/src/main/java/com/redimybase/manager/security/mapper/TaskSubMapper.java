package com.redimybase.manager.security.mapper;

import com.redimybase.manager.security.entity.TaskSubEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redimybase.manager.security.entity.dto.TaskSubDTO;
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
