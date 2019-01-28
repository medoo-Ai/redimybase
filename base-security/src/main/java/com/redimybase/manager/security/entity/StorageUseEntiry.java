package com.redimybase.manager.security.entity;

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
 * 入库与领用
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_storage_use")
public class StorageUseEntiry extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

        /**
     * 操作人ID
     */
         @TableField("operator_id")
    private String operatorId;

        /**
     * 物料ID
     */
         @TableField("device_id")
    private String deviceId;

        /**
     * 数量
     */
         @TableField("count")
    private BigDecimal count;

        /**
     * 操作类型(1:入库,2:领用)
     */
         @TableField("operational_type")
    private Integer operationalType;

        /**
     * 备注
     */
         @TableField("remark")
    private String remark;

        /**
     * 创建人ID
     */
         @TableField("creator_id")
    private String creatorId;

        /**
     * 创建人
     */
         @TableField("creator")
    private String creator;

        /**
     * 最后修改人ID
     */
         @TableField("reviser_id")
    private String reviserId;

        /**
     * 最后修改人
     */
         @TableField("reviser")
    private String reviser;

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
