package com.aispread.manager.devicetype.dto;

import com.aispread.manager.devicetype.entity.DeviceTypeEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;


/**
 * 设备类型树结构DTO
 */
@Data
public class DeviceTypeDTO {

  @ApiModelProperty(value="id")
  private String id;

  /**
   * 类型名称
   */
  @ApiModelProperty(value="类型名称")
  private String name;

  /**
   * 子类型
   */
  @ApiModelProperty(value="子类型列表")
  private List<DeviceTypeDTO> subType;

}
