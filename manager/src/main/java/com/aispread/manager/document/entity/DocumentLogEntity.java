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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文档库操作日志表
 * </p>
 *
 * @author vim
 * @since 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_document_log")
public class DocumentLogEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 日志名称
     */
    @TableField("name")
    private String name;

    /**
     * 操作日志内容
     */
    @TableField("content")
    private String content;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") private Date createTime;

    /**
     * 创建人
     */
    @TableField("creator")
    private String creator;

    /**
     * 创建人ID
     */
    @TableField("creator_id")
    private String creatorId;

    /**
     * 文档ID
     */
    @TableField("document_id")
    private String documentId;

    /**
     * 文档名称
     */
    @TableField("document_name")
    private String documentName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
