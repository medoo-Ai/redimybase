package com.aispread.manager.device.entity;

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
 * 设备主表
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_device")
public class DeviceEntity extends IdEntity<String> {

  private static final long serialVersionUID = 1L;
  public static class Status{
    public static final Integer 删除 = 0;
    public static final Integer 启用=1;
    public static final Integer 禁用 = 2;
  }
  @TableId(value = "id", type = IdType.ID_WORKER_STR)
  @ApiModelProperty(value="id")
  private String id;


  /**
   * 设备名称-状态不为0的数据里面唯一
   */
  @TableField("name")
  @ApiModelProperty(value="设备名称-状态不为0的数据里面唯一")
  private String name;


  /**
   * 设备编码-预留字段
   */
  @TableField("code")
  @ApiModelProperty(value="设备编码-预留字段")
  private String code;

  /**
   * 设备类型ID
   */
  @TableField("type_id")
  @ApiModelProperty(value="设备类型ID")
  private String typeId;

  /**
   * 品牌ID
   */
  @TableField("brand_id")
  @ApiModelProperty(value="品牌ID")
  private String brandId;

  /**
   * 规格型号ID
   */
  @TableField("model_id")
  @ApiModelProperty(value="规格型号ID")
  private String modelId;

  /**
   * 单位ID
   */
  @TableField("unit_id")
  @ApiModelProperty(value="单位ID")
  private String unitId;

  /**
   * 描述
   */
  @TableField("remark")
  @ApiModelProperty(value="描述")
  private String remark;

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
  @ApiModelProperty(value="最后修改人ID")
  private String updateBy;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
