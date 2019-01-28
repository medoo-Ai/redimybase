package com.aispread.manager.flowable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流程业务表
 * </p>
 *
 * @author vim
 * @since 2019-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_flow_business")
public class FlowBusinessEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    /**
     * 表单号
     */
    @TableId(value = "id")
    private String id;

    /**
     * 表单名称
     */
    @TableField("name")
    private String name;

    /**
     * 种类ID
     */
    @TableField("category_id")
    private String categoryId;

    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
