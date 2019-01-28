package com.aispread.manager.flowable.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("act_hi_taskinst")
public class ActHiTaskinstEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId("ID_")
    private String id;

    @TableField("REV_")
    private Integer rev;

    @TableField("PROC_DEF_ID_")
    private String procDefId;

    @TableField("TASK_DEF_ID_")
    private String taskDefId;

    @TableField("TASK_DEF_KEY_")
    private String taskDefKey;

    @TableField("PROC_INST_ID_")
    private String procInstId;

    @TableField("EXECUTION_ID_")
    private String executionId;

    @TableField("SCOPE_ID_")
    private String scopeId;

    @TableField("SUB_SCOPE_ID_")
    private String subScopeId;

    @TableField("SCOPE_TYPE_")
    private String scopeType;

    @TableField("SCOPE_DEFINITION_ID_")
    private String scopeDefinitionId;

    @TableField("NAME_")
    private String name;

    @TableField("PARENT_TASK_ID_")
    private String parentTaskId;

    @TableField("DESCRIPTION_")
    private String description;

    @TableField("OWNER_")
    private String owner;

    @TableField("ASSIGNEE_")
    private String assignee;

    @TableField("START_TIME_")
    private LocalDateTime startTime;

    @TableField("CLAIM_TIME_")
    private LocalDateTime claimTime;

    @TableField("END_TIME_")
    private LocalDateTime endTime;

    @TableField("DURATION_")
    private Long duration;

    @TableField("DELETE_REASON_")
    private String deleteReason;

    @TableField("PRIORITY_")
    private Integer priority;

    @TableField("DUE_DATE_")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueDate;

    @TableField("FORM_KEY_")
    private String formKey;

    @TableField("CATEGORY_")
    private String category;

    @TableField("TENANT_ID_")
    private String tenantId;

    @TableField("LAST_UPDATED_TIME_")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdatedTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
