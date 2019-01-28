package com.aispread.manager.flowable.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 审批人分发任务请求
 * Created by Vim 2019/1/18 13:47
 *
 * @author Vim
 */
@Data
@ApiModel(value = "审批人分发任务请求")
public class FlowDistributeRequest implements Serializable {

    /**
     * 任务ID
     */
    @ApiModelProperty(value = "任务ID")
    private String taskId;

    /**
     * 任务节点CODE
     */
    @ApiModelProperty(value = "任务节点CODE")
    private String taskCode;

    /**
     * 业务ID
     */
    @ApiModelProperty(value = "业务ID")
    private String businessId;

    /**
     * 分发附件ID
     */
    @ApiModelProperty(value = "分发附件ID")
    private String attachmentId;

    /**
     * 分发任务内容
     */
    @ApiModelProperty(value = "分发任务内容")
    private String content;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 签收人
     */
    @ApiModelProperty(value = "签收人")
    private String assignee;

}
