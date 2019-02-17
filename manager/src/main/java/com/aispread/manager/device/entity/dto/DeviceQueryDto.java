package com.aispread.manager.device.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceQueryDto extends DeviceDto{
  /**
   * 页数
   */
  @ApiModelProperty(value="页数")
  private Integer P_NO;

  /**
   * 数据大小
   */
  @ApiModelProperty(value="数据大小")
  private Integer P_SIZE;

  /**
   * 排序字段
   */
  @ApiModelProperty(value="排序字段")
  private String orderBy;

  /**
   * 是否顺序排序
   */
  @ApiModelProperty(value="是否顺序排序")
  private boolean asc;

}
