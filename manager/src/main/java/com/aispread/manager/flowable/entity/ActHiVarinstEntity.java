package com.aispread.manager.flowable.entity;

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
 *
 * </p>
 *
 * @author vim
 * @since 2019-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("act_hi_varinst")
public class ActHiVarinstEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID_")
    private String id;

    @TableField("REV_")
    private Integer rev;

    @TableField("PROC_INST_ID_")
    private String procInstId;

    @TableField("EXECUTION_ID_")
    private String executionId;

    @TableField("TASK_ID_")
    private String taskId;

    @TableField("NAME_")
    private String name;

    @TableField("VAR_TYPE_")
    private String varType;

    @TableField("SCOPE_ID_")
    private String scopeId;

    @TableField("SUB_SCOPE_ID_")
    private String subScopeId;

    @TableField("SCOPE_TYPE_")
    private String scopeType;

    @TableField("BYTEARRAY_ID_")
    private String bytearrayId;

    @TableField("DOUBLE_")
    private Double doubleType;

    @TableField("LONG_")
    private Long longType;

    @TableField("TEXT_")
    private String text;

    @TableField("TEXT2_")
    private String text2;

    @TableField("CREATE_TIME_")
    private LocalDateTime createTime;

    @TableField("LAST_UPDATED_TIME_")
    private LocalDateTime lastUpdatedTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
