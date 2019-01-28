package com.redimybase.manager.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.manager.security.entity.InventoryEntity;
import com.redimybase.manager.security.entity.dto.InventoryUpdateDTO;

/**
 * <p>
 * 设备库存表 服务类
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-07
 */
public interface InventoryService extends IService<InventoryEntity>{
  boolean updateInventory(InventoryUpdateDTO dto) throws BusinessException;
}
