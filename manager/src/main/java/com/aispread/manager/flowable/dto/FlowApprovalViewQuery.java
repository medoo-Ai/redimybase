package com.aispread.manager.flowable.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 待办查看视图查询条件
 * Created by Vim 2019/1/15 13:37
 *
 * @author Vim
 */
@Data
@ApiModel(value = "待办查看视图查询条件")
public class FlowApprovalViewQuery implements Serializable {
    @ApiModelProperty(value = "任务ID")
    private String taskId;
    @ApiModelProperty(value = "业务ID")
    private String businessId;
    @ApiModelProperty(value = "是否为手机端")
    private boolean mobile;

    /**
     * 查询动作(0:待办,1:已办,2:已申请)
     */
    @ApiModelProperty(value = "查询动作(0:待办,1:已办,2:已申请)")
    private Integer action;

    public static class Action{
        public static final Integer 待办 = 0;
        public static final Integer 已办 = 1;
        public static final Integer 已申请 = 2;
    }

}
