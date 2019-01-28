package com.redimybase.manager.security.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.manager.security.entity.DeviceEntity;
import com.redimybase.manager.security.entity.dto.DeviceListDTO;
import com.redimybase.manager.security.entity.dto.UserAddressListDTO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备主表 Mapper 接口
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-07
 */
public interface DeviceMapper extends BaseMapper<DeviceEntity> {
  public List<DeviceListDTO> getDeviceList(DeviceListDTO query);

  public boolean update(DeviceEntity entity);

//  public Page applyTaskList(Page<DeviceEn> page, @Param("query") FlowApplyTaskQuery query);

}
