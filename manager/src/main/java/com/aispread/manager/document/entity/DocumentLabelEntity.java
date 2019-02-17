package com.aispread.manager.document.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

import com.redimybase.framework.mybatis.id.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文档标签表
 * </p>
 *
 * @author vim
 * @since 2019-01-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_document_label")
@ApiModel("文档标签实体")
public class DocumentLabelEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 标签名
     */
    @TableField("name")
    @ApiModelProperty("标签名")
    private String name;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人ID
     */
    @TableField("create_id")
    @ApiModelProperty("创建人ID")
    private String createId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
