package com.aispread.manager.document.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.redimybase.framework.mybatis.id.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文档表
 * </p>
 *
 * @author vim
 * @since 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_document")
@ApiModel("文档实体")
public class DocumentEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 文件名称
     */
    @TableField("name")
    @ApiModelProperty("文件名称")
    private String name;

    /**
     * 附件ID
     */
    @TableField("attachment_id")
    @ApiModelProperty("附件ID")
    private String attachmentId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") private Date createTime;

    /**
     * 创建人
     */
    @TableField("creator")
    @ApiModelProperty("创建人")
    private String creator;

    /**
     * 创建人ID
     */
    @TableField("creator_id")
    @ApiModelProperty("创建人ID")
    private String creatorId;

    /**
     * 状态(0:正常,1:删除)
     */
    @ApiModelProperty("状态(0:正常,1:删除)")
    private Integer status;

    /**
     * 可视范围(1:个人,2:部门,3:公司)
     */
    @ApiModelProperty("可视范围(1:个人,2:部门,3:公司)")
    private Integer viewRange;

    /**
     * 目录ID
     */
    @ApiModelProperty("目录ID")
    private String folderId;


    @TableField(exist = false)
    @ApiModelProperty("目录名称")
    private String folderName;

    @TableField(exist = false)
    @ApiModelProperty("标签")
    private String label;

    @ApiModelProperty("文档后缀名")
    @TableField(exist = false)
    private String suffixType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getLabel() {
        return this.label != null ? label.replaceAll(",", "/") : null;
    }

    public static class Status {
        public static final Integer 正常 = 0;
        public static final Integer 删除 = 1;
    }

    public static class ViewRange {
        public static final Integer 个人 = 1;
        public static final Integer 部门 = 2;
        public static final Integer 公司 = 3;
    }
}
