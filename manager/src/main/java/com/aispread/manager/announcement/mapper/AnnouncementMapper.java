package com.aispread.manager.announcement.mapper;

import com.aispread.manager.announcement.entity.AnnouncementEntity;
import com.aispread.manager.announcement.entity.AnnouncementListDTO;
import com.aispread.manager.announcement.entity.AnnouncementQueryDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redimybase.framework.exception.BusinessException;
import com.aispread.manager.jointtask.entity.TaskMainDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 通知公告 Mapper 接口
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-26
 */
public interface AnnouncementMapper extends BaseMapper<AnnouncementEntity> {

  AnnouncementListDTO getDTO(String id) throws BusinessException;
  IPage<AnnouncementListDTO> getDTOList(IPage<TaskMainDTO> page ,@Param("query") AnnouncementQueryDTO query);
  boolean switchToAnnouncement(String id);
  boolean switchToImg(String id);
}
