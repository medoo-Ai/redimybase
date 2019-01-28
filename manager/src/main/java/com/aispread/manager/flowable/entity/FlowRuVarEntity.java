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
 * 运行流程变量表
 * </p>
 *
 * @author vim
 * @since 2019-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_flow_ru_var")
public class FlowRuVarEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 工作流节点CODE
     */
    @TableField("task_code")
    private String taskCode;

    /**
     * 业务ID
     */
    @TableField("business_id")
    private String businessId;

    /**
     * 变量表达式名称
     */
    @TableField("name")
    private String name;

    /**
     * 表单字段属性
     */
    @TableField("form_field")
    private String formField;

    /**
     * 默认值
     */
    @TableField("default_value")
    private String defaultValue;

    /**
     * 表达式
     */
    @TableField("expression")
    private String expression;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 逻辑运算符
     */
    private Integer symbol;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
