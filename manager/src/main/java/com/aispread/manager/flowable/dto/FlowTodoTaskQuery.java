package com.aispread.manager.flowable.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 待办任务查询条件
 * Created by Vim 2019/1/9 11:29
 *
 * @author Vim
 */
@Data
@ApiModel(value = "待办任务查询条件")
public class FlowTodoTaskQuery extends FlowAppTaskInfoQuery {

    /**
     * 审批人
     */
    @ApiModelProperty(value = "审批人")
    private String userName;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 审批环节
     */
    @ApiModelProperty(value = "审批环节")
    private String nodeName;


    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    /**
     * 截止时间
     */
    @ApiModelProperty(value = "截止时间")
    private String endTime;

    /**
     * 流程定义种类ID
     */
    @ApiModelProperty(value = "流程定义种类ID")
    private String categoryId;


    /**
     * 页数
     */
    @ApiModelProperty(value = "页数")
    private Integer page;

    /**
     * 数据大小
     */
    @ApiModelProperty(value = "数据大小")
    private Integer pageSize;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private String orderBy;

    /**
     * 是否顺序排序
     */
    @ApiModelProperty(value = "是否顺序排序")
    private boolean asc;


    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;

}
