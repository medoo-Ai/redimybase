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
 * 流程任务代理人表
 * </p>
 *
 * @author vim
 * @since 2019-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_flow_agent")
public class FlowAgentEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 委托人ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 代理人ID
     */
    @TableField("agent_id")
    private String agentId;

    /**
     * 流程定义ID
     */
    @TableField("definition_id")
    private String definitionId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 是否继续保存
     */
    @TableField(exist = false)
    private Integer continueSave;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
