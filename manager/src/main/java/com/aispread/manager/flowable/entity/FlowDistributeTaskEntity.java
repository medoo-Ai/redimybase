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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流程分发任务表
 * </p>
 *
 * @author vim
 * @since 2019-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_flow_distribute_task")
@ApiModel(value = "流程分发任务实体")
public class FlowDistributeTaskEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 标题
     */
    @TableField("title")
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 分发内容
     */
    @TableField("content")
    @ApiModelProperty(value = "分发内容")
    private String content;

    /**
     * 附件ID
     */
    @TableField("attachment_id")
    @ApiModelProperty(value = "附件ID")
    private String attachmentId;

    /**
     * 任务ID
     */
    @TableField("task_id")
    @ApiModelProperty(value = "任务ID")
    private String taskId;

    /**
     * 任务CODE
     */
    @TableField("task_code")
    @ApiModelProperty(value = "任务CODE")
    private String taskCode;

    /**
     * 业务ID
     */
    @TableField("business_id")
    @ApiModelProperty(value = "业务ID")
    private String businessId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 签收人
     */
    @TableField("assignee")
    @ApiModelProperty(value = "签收人")
    private String assignee;

    /**
     * 状态(1:待回馈,2:已回馈)
     */
    @ApiModelProperty(value = "状态(1:待回馈,2:已回馈)")
    private Integer status;

    /**
     * 反馈内容
     */
    @ApiModelProperty(value = "反馈内容")
    private String feedbackContent;

    /**
     * 反馈附件ID
     */
    @ApiModelProperty(value = "反馈附件ID")
    private String feedbackAttachmentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Status {
        public static final Integer 待回馈 = 1;
        public static final Integer 已回馈 = 2;
    }

}
