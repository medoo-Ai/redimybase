package com.aispread.manager.flowable.entity;

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
 *
 * </p>
 *
 * @author vim
 * @since 2019-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("act_hi_procinst")
public class ActHiProcinstEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId("ID_")
    private String id;

    @TableField("REV_")
    private Integer rev;

    @TableField("PROC_INST_ID_")
    private String procInstId;

    @TableField("BUSINESS_KEY_")
    private String businessKey;

    @TableField("PROC_DEF_ID_")
    private String procDefId;

    @TableField("START_TIME_")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @TableField("END_TIME_")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @TableField("DURATION_")
    private Long duration;

    @TableField("START_USER_ID_")
    private String startUserId;

    @TableField("START_ACT_ID_")
    private String startActId;

    @TableField("END_ACT_ID_")
    private String endActId;

    @TableField("SUPER_PROCESS_INSTANCE_ID_")
    private String superProcessInstanceId;

    @TableField("DELETE_REASON_")
    private String deleteReason;

    @TableField("TENANT_ID_")
    private String tenantId;

    @TableField("NAME_")
    private String name;

    @TableField("CALLBACK_ID_")
    private String callbackId;

    @TableField("CALLBACK_TYPE_")
    private String callbackType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
