package com.redimybase.manager.security.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.manager.security.entity.TaskMainEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redimybase.manager.security.entity.dto.TaskMainDTO;
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
