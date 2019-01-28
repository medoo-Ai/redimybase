package com.redimybase.manager.security.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 设备列表DTO
 */
@Data
public class DeviceListDTO {

  private String id;

  /**
   * 设备编码
   */
  private String code;

  /**
   * 设备名称
   */
  private String name;

  /**
   * 设备类型ID
   */
  private String typeId;

  /**
   * 设备类型名称
   */
  private String typeName;


  /**
   * 型号
   */
  private String model;

  /**
   * 单位
   */
  private String unit;

  /**
   * 描述
   */
  private String remark;

  /**
   * 创建人ID
   */
  private String creatorId;

  /**
   * 创建人
   */
  private String creatorName;

  /**
   * 最后修改人ID
   */
  private String reviserId;

  /**
   * 最后修改人
   */
  private String reviserName;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;

  /**
   * 最后更新时间
   */
  private LocalDateTime updateTime;

}
