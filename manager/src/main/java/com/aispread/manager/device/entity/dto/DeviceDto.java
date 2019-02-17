package com.aispread.manager.device.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class DeviceDto implements Serializable {
  private static final long serialVersionUID = 1L;
  @ApiModelProperty(value="")
  private String id;

  /**
   * 设备名称-状态不为0的数据里面唯一
   */
  @ApiModelProperty(value="设备名称-状态不为0的数据里面唯一")
  private String name;

  /**
   * 设备编码-预留字段
   */
  /*@TableField("code")
  @ApiModelProperty(value="设备编码-预留字段")
  private String code;*/

  /**
   * 设备类型ID
   */
  @ApiModelProperty(value="设备类型ID")
  private String typeId;

  /**
   * 设备类型名称
   */
  @ApiModelProperty(value="设备类型名称")
  private String typeName;

  /**
   * 父级设备类型ID
   */
  @ApiModelProperty(value="父级设备类型ID")
  private String parentTypeId;

  /**
   * 父级设备类型名称
   */
  @ApiModelProperty(value="父级设备类型名称")
  private String parentTypeName;

  /**
   * 品牌ID
   */
  @ApiModelProperty(value="品牌ID")
  private String brandId;

  /**
   * 品牌名称
   */
  @ApiModelProperty(value="品牌名称")
  private String brandName;

  /**
   * 规格型号ID
   */
  @ApiModelProperty(value="规格型号ID")
  private String modelId;

  /**
   * 规格型号名称
   */
  @ApiModelProperty(value="规格型号名称")
  private String modelName;

  /**
   * 单位ID
   */
  @ApiModelProperty(value="单位ID")
  private String unitId;

  /**
   * 单位名称
   */
  @ApiModelProperty(value="单位名称")
  private String unitName;

  /**
   * 描述
   */
  @ApiModelProperty(value="描述")
  private String remark;

  /**
   * 状态(0:删除,1:启用,2:禁用)
   */
  @ApiModelProperty(value="状态(0:删除,1:启用,2:禁用)")
  private Integer status;

  /**
   * 创建时间
   */
  @ApiModelProperty(value="创建时间,该字段暂不支持过滤查询")
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  private Date createAt;

  /**
   * 创建人ID
   */
  @ApiModelProperty(value="创建人ID")
  private String createBy;

  /**
   * 创建人名称
   */
  @ApiModelProperty(value="创建人名称")
  private String createName;

  /**
   * 最后更新时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  @ApiModelProperty(value="最后更新时间,该字段暂不支持过滤查询")
  private Date updateAt;

  /**
   * 最后修改人ID
   */
  @ApiModelProperty(value="最后修改人ID")
  private String updateBy;

  /**
   * 最后修改人名称
   */
  @ApiModelProperty(value="最后修改人名称")
  private String updateName;
}
