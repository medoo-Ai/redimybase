package com.aispread.manager.flowable.entity;

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
 * 流程按钮表
 * </p>
 *
 * @author vim
 * @since 2019-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_flow_button")
public class FlowButtonEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 节点ID
     */
    @TableField("node_id")
    private String nodeId;

    /**
     * 按钮操作类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") private Date createTime;

    /**
     * 排序
     */
    private Integer sort;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Type{
        public static final Integer 同意 = 1;
        public static final Integer 驳回 = 2;
        public static final Integer 转办 = 3;
        public static final Integer 沟通 = 4;
        public static final Integer 留言 = 5;
        public static final Integer 加签 = 6;
        public static final Integer 提交 = 7;
        public static final Integer 撤回 = 8;
        public static final Integer 分发 = 9;
        public static final Integer 反馈 = 10;
    }
}
