package com.redimybase.manager.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.manager.security.entity.DeviceEntity;
import com.redimybase.manager.security.entity.DeviceTypeEntity;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.mapper.DeviceTypeMapper;
import com.redimybase.manager.security.service.DeviceTypeService;
import com.redimybase.security.utils.SecurityUtil;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备类型 服务实现类
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-07
 */
@Service
public class DeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceTypeEntity> implements
    DeviceTypeService {

  @Override
  public boolean updateById(DeviceTypeEntity entity) {
    entity.setUpdateTime(LocalDateTime.now());
    UserEntity currentUser = SecurityUtil.getCurrentUser();
    entity.setReviserId(currentUser.getId());
    return super.updateById(entity);
  }

  @Override
  public boolean removeById(Serializable id) {
    DeviceTypeEntity byId = getById(id);
    byId.setStatus(0);
    return updateById(byId);
  }
  @Override
  public boolean removeByIds(Collection<? extends Serializable> idList) {
    idList.stream().forEach(id -> {
      DeviceTypeEntity byId = getById(id);
      byId.setStatus(0);
      updateById(byId);
    });
    return true;
  }

  @Override
  public boolean save(DeviceTypeEntity entity) {
    entity.setCreateTime(LocalDateTime.now());
    UserEntity currentUser = SecurityUtil.getCurrentUser();
    entity.setCreatorId(currentUser.getId());
    return super.save(entity);
  }
}
