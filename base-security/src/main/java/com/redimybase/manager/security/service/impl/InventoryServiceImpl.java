package com.redimybase.manager.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.manager.security.entity.InventoryEntity;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.entity.dto.InventoryUpdateDTO;
import com.redimybase.manager.security.mapper.InventoryMapper;
import com.redimybase.manager.security.service.InventoryService;
import com.redimybase.security.utils.SecurityUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备库存表 服务实现类
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-07
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, InventoryEntity> implements
    InventoryService {
  @Override
  public boolean updateById(InventoryEntity entity) {
    entity.setUpdateTime(LocalDateTime.now());
    UserEntity currentUser = SecurityUtil.getCurrentUser();
    entity.setReviserId(currentUser.getId());
    return super.updateById(entity);
  }

  @Override
  public boolean updateInventory(InventoryUpdateDTO dto) throws BusinessException {
    /*校验是否存在库存信息*/
    QueryWrapper<InventoryEntity> wrapper = new QueryWrapper<>();
    wrapper.and(i -> i.eq("device", dto.getDeviceId()));
    InventoryEntity entity = getOne(wrapper);
    if(entity == null){
      throw new BusinessException(R.失败,"该设备不存在库存信息");
    }
    /*校验库存量*/
    if(entity.getQty().add(dto.getOffsetValue()).compareTo(BigDecimal.ZERO) == -1){
      throw new BusinessException(R.失败,"对应设备库存不足");
    }
    return baseMapper.updateInventory(dto);
  }

  @Override
  public boolean save(InventoryEntity entity) {
    entity.setCreateTime(LocalDateTime.now());
    UserEntity currentUser = SecurityUtil.getCurrentUser();
    entity.setCreatorId(currentUser.getId());
    return super.save(entity);
  }
}
