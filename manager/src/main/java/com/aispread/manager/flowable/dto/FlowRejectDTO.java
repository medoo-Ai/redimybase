package com.aispread.manager.flowable.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 流程驳回DTO
 * Created by Vim 2019/1/11 20:47
 *
 * @author Vim
 */
@Data
@ApiModel(value = "流程驳回model")
public class FlowRejectDTO implements Serializable {

    /**
     * 任务ID
     */
    @ApiModelProperty(value = "任务ID")
    private String taskId;

    /**
     * 驳回类型(1:上一节点,2:发起人,3:到指定节点)
     */
    @ApiModelProperty(value = "驳回类型(1:上一节点,2:发起人,3:到指定节点)")
    private Integer type;

    /**
     * 意见
     */
    @ApiModelProperty(value = "意见")
    private String comment;

    /**
     * 附件ID
     */
    @ApiModelProperty(value = "附件ID")
    private String attachmentId;

    /**
     * 指定节点的任务节点CODE
     */
    @ApiModelProperty(value = "指定节点的任务节点CODE")
    private String targetTaskCode;

    /**
     * 业务ID
     */
    @ApiModelProperty(value = "业务ID")
    private String businessId;


}
