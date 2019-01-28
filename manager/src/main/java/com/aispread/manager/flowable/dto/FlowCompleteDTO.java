package com.aispread.manager.flowable.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 流程完成任务DTO
 * Created by Vim 2019/1/10 1
 *
 * @author Vim
 */
@Data
@ApiModel(value = "流程完成任务model")
public class FlowCompleteDTO implements Serializable {

    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "流程变量")
    private String variables;

    @ApiModelProperty(value = "业务ID")
    private String businessId;

    @ApiModelProperty(value = "运行表单ID")
    private String runFormId;

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
     * 审批动作(0:拒绝,1:同意,2:提交)
     */
    @ApiModelProperty(value = "审批动作(0:拒绝,1:同意,2:提交)")
    private Integer action;
}
