package com.redimybase.manager.security.entity.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class InventoryUpdateDTO {

  private String deviceId;

  /*库存偏移值,正数时增加,负数时扣减*/
  private BigDecimal offsetValue;
}
