package com.redimybase.manager.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.redimybase.framework.mybatis.id.IdEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备类型
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_device_type")
public class DeviceTypeEntity extends IdEntity<String> {

  private static final long serialVersionUID = 1L;


  @TableId(value = "id", type = IdType.ID_WORKER_STR)
  private String id;

  /**
   * 类型编码
   */
  @TableField("code")
  private String code;

  /**
   * 类型名称
   */
  @TableField("name")
  private String name;


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
   * 状态(0:删除,1:启用,2:禁用)
   */
  @TableField("status")
  private Integer status;

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

  public static class Status{
    public static final Integer 删除 = 0;
    public static final Integer 启用=1;
    public static final Integer 禁用 = 2;
  }
}
