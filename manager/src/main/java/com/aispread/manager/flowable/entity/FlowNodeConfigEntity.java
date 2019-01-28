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
 * 流程节点配置表
 * </p>
 *
 * @author vim
 * @since 2019-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_flow_node_config")
public class FlowNodeConfigEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 节点ID
     */
    @TableField("node_id")
    private String nodeId;

    /**
     * 驳回节点类型(1:上一节点,2:发起人3,直接结束)
     */
    @TableField("reject_node_type")
    private Integer rejectNodeType;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public static class Type {
        public static final Integer 上一节点 = 1;
        public static final Integer 发起人 = 2;
        public static final Integer 直接结束 = 3;
    }
}
