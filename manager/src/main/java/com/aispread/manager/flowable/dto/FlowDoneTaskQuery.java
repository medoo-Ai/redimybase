package com.aispread.manager.flowable.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 已办任务查询条件
 * Created by Vim 2019/1/11 18:44
 *
 * @author Vim
 */
@Data
@ApiModel(value = "已办任务查询条件")
public class FlowDoneTaskQuery extends FlowAppTaskInfoQuery {

    /**
     * 已办任务对应的用户ID
     */
    @ApiModelProperty(value = "已办任务对应的用户ID")
    private String userId;

    /**
     * 业务ID
     */
    @ApiModelProperty(value = "业务ID")
    private String businessId;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 申请开始时间
     */
    @ApiModelProperty(value = "申请开始时间")
    private String applyStartTime;

    /**
     * 申请结束时间
     */
    @ApiModelProperty(value = "申请结束时间")
    private String applyEndTime;

    /**
     * 审批开始时间
     */
    @ApiModelProperty(value = "审批开始时间")
    private String approvalStartTime;

    /**
     * 审批结束时间
     */
    @ApiModelProperty(value = "审批结束时间")
    private String approvalEndTime;

    /**
     * 流程种类ID
     */
    @ApiModelProperty(value = "流程种类ID")
    private String categoryId;


    private Integer page;

    private Integer pageSize;

    private String orderBy;

    private boolean asc;
}
