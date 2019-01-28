package com.aispread.manager.integrate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import com.redimybase.framework.mybatis.id.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统集成表
 * </p>
 *
 * @author vim
 * @since 2019-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_system_integrate")
@ApiModel("系统集成实体")
public class SystemIntegrateEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 系统名称
     */
    @ApiModelProperty("系统名称")
    @TableField("name")
    private String name;

    /**
     * 系统链接
     */
    @TableField("url")
    @ApiModelProperty("系统链接")
    private String url;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @ApiModelProperty("用户ID")
    private String userId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
