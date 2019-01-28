package com.redimybase.manager.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redimybase.manager.security.entity.InventoryEntity;
import com.redimybase.manager.security.entity.dto.InventoryUpdateDTO;

/**
 * <p>
 * 设备库存表 Mapper 接口
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-07
 */
public interface InventoryMapper extends BaseMapper<InventoryEntity> {
  boolean updateInventory(InventoryUpdateDTO dto);
}
