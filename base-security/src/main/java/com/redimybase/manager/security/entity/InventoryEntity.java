package com.redimybase.manager.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.redimybase.framework.mybatis.id.IdEntity;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备库存表
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_inventory")
public class InventoryEntity extends IdEntity<String> {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.ID_WORKER_STR)
  private String id;

  /**
   * 设备类型ID
   */
  @TableField("type")
  private String type;

  /**
   * 设备ID
   */
  @TableField("device")
  private String device;

  /**
   * 数量
   */
  @TableField("qty")
  private BigDecimal qty;


  /**
   * 创建人ID
   */
  @TableField("creator_id")
  private String creatorId;

  /**
   * 最后修改人ID
   */
  @TableField("reviser_id")
  private String reviserId;

  /**
   * 创建时间
   */
  @TableField("create_time")
  private LocalDateTime createTime;

  /**
   * 最后更新时间
   */
  @TableField("update_time")
  private LocalDateTime updateTime;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
