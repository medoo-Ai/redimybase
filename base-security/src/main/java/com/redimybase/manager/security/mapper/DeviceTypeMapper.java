package com.redimybase.manager.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redimybase.manager.security.entity.DeviceTypeEntity;
import com.redimybase.manager.security.entity.dto.DeviceListDTO;
import java.util.List;

/**
 * <p>
 * 设备类型 Mapper 接口
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-07
 */
public interface DeviceTypeMapper extends BaseMapper<DeviceTypeEntity> {
  public List<DeviceListDTO> getDeviceTypeList(DeviceListDTO query);
}
