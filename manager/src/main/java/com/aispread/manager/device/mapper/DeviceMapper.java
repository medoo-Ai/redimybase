package com.aispread.manager.device.mapper;

import com.aispread.manager.device.entity.DeviceEntity;
import com.aispread.manager.device.entity.dto.DeviceDto;
import com.aispread.manager.device.entity.dto.DeviceQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备主表 Mapper 接口
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-29
 */
public interface DeviceMapper extends BaseMapper<DeviceEntity> {
  IPage<DeviceDto> getDeviceDTOList(IPage<DeviceQueryDto> page ,@Param("query") DeviceQueryDto query);
}
