package com.redimybase.manager.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redimybase.manager.security.entity.DeviceEntity;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.entity.dto.DeviceListDTO;
import com.redimybase.manager.security.mapper.DeviceMapper;
import java.util.List;

/**
 * <p>
 * 设备主表 服务类
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-07
 */
public interface DeviceService extends IService<DeviceEntity> {
    public List<DeviceListDTO> getDeviceList(DeviceListDTO query);
}
