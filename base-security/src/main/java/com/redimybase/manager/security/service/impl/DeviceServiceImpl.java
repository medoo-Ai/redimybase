package com.redimybase.manager.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.manager.security.entity.DeviceEntity;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.entity.dto.DeviceListDTO;
import com.redimybase.manager.security.mapper.DeviceMapper;
import com.redimybase.manager.security.service.DeviceService;
import com.redimybase.security.utils.SecurityUtil;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备主表 服务实现类
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-07
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, DeviceEntity> implements DeviceService {

  @Override
  public List<DeviceListDTO> getDeviceList(DeviceListDTO query) {
    return baseMapper.getDeviceList(query);
  }

  @Override
  public boolean save(DeviceEntity entity) {
    entity.setCreateTime(LocalDateTime.now());
    UserEntity currentUser = SecurityUtil.getCurrentUser();
    entity.setCreatorId(currentUser.getId());
    return super.save(entity);
  }

  @Override
  public boolean removeById(Serializable id) {
    DeviceEntity byId = getById(id);
    byId.setStatus(0);
    return updateById(byId);
  }

  @Override
  public boolean removeByIds(Collection<? extends Serializable> idList) {
    idList.stream().forEach(id -> {
      DeviceEntity byId = getById(id);
      byId.setStatus(0);
      updateById(byId);
    });
    return true;
  }

  @Override
  public boolean updateById(DeviceEntity entity) {
    entity.setUpdateTime(LocalDateTime.now());
    UserEntity currentUser = SecurityUtil.getCurrentUser();
    entity.setReviserId(currentUser.getId());
    return super.updateById(entity);
  }
}
