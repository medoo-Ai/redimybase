package com.aispread.manager.flowable.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;

/**
 * 已申请任务查询条件
 * Created by Vim 2019/1/11 19:39
 *
 * @author Vim
 */
@Data
@ApiModel("已申请任务查询条件")
public class FlowApplyTaskQuery extends FlowAppTaskInfoQuery {


    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 状态(0:运行中,1:正常结束)
     */
    @ApiModelProperty(value = "状态(0:运行中,1:正常结束)")
    private String status;

    /**
     * 申请开始时间
     */
    @ApiModelProperty(value = "申请开始时间")
    private String startTime;

    /**
     * 申请结束时间
     */
    @ApiModelProperty(value = "申请结束时间")
    private String endTime;

    /**
     * 页码
     */
    @ApiModelProperty(value = "页码")
    private Integer page;

    /**
     * 当前页数据大小
     */
    @ApiModelProperty(value = "当前页数据大小")
    private Integer pageSize;

    /**
     * 根据哪个字段排序
     */
    @ApiModelProperty(value = "根据哪个字段排序")
    private String orderBy;

    /**
     * 是否顺序排序(0:否-倒序,1:是-顺序)
     */
    @ApiModelProperty(value = "是否顺序排序(0:否-倒序,1:是-顺序)")
    private boolean asc;

    /**
     * 流程种类ID
     */
    @ApiModelProperty(value = "流程种类ID")
    private String categoryId;
}
