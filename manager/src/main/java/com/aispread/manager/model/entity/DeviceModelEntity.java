package com.aispread.manager.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redimybase.framework.mybatis.id.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备从表-规格/型号
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_model")
public class DeviceModelEntity extends IdEntity<String> {

  private static final long serialVersionUID = 1L;

  public static class Status{
    public static final Integer 删除 = 0;
    public static final Integer 启用=1;
    public static final Integer 禁用 = 2;
  }

  @TableId(value = "id", type = IdType.ID_WORKER_STR)
  @ApiModelProperty(value="ID")
  private String id;

  /**
   * 名称
   */
  @TableField("name")
  @ApiModelProperty(value="名称")
  private String name;

  /**
   * 上级-品牌ID
   */
  @TableField("brand_id")
  @ApiModelProperty(value="上级-品牌ID")
  private String brandId;

  /**
   * 状态(0:删除,1:启用,2:禁用)
   */
  @TableField("status")
  @ApiModelProperty(value="状态(0:删除,1:启用,2:禁用)")
  private Integer status;

  /**
   * 创建时间
   */
  @TableField("create_at")
  @ApiModelProperty(value="创建时间")
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  private Date createAt;

  /**
   * 创建人ID
   */
  @TableField("create_by")
  @ApiModelProperty(value="创建人ID")
  private String createBy;

  /**
   * 最后更新时间
   */
  @TableField("update_at")
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  @ApiModelProperty(value="最后更新时间")
  private Date updateAt;

  /**
   * 最后修改人ID
   */
  @TableField("update_by")
  @ApiModelProperty(value="update_by")
  private String updateBy;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
