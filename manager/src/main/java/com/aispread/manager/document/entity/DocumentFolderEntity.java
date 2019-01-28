package com.aispread.manager.document.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文档目录表
 * </p>
 *
 * @author vim
 * @since 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_document_folder")
public class DocumentFolderEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 目录名称
     */
    @TableField("name")
    private String name;

    /**
     * 目录code
     */
    @TableField("code")
    private String code;

    /**
     * 父级ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

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
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 目录标签
     */
    @TableField("label")
    private String label;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
