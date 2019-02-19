package com.aispread.manager.jointtask.mapper;

import com.aispread.manager.jointtask.entity.TaskMainDTO;
import com.aispread.manager.jointtask.entity.TaskMainEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redimybase.framework.exception.BusinessException;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 协同任务 Mapper 接口
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-10
 */
public interface TaskMainMapper extends BaseMapper<TaskMainEntity> {
  TaskMainDTO getDTOById(String id) throws BusinessException;
  IPage<TaskMainDTO> getTaskMainList(IPage<TaskMainDTO> page ,@Param("query") TaskMainDTO query);

//  @Override
//  IPage<TaskMainEntity> selectPage(IPage<TaskMainEntity> iPage, Wrapper<TaskMainEntity> wrapper);
}
