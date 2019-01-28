package com.aispread.manager.flowable.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 已办任务详情查询
 * Created by Vim 2019/1/15 19:07
 *
 * @author Vim
 */
@Data
@ApiModel(value = "已办任务详情查询")
public class FlowDoneViewQuery implements Serializable {

    @ApiModelProperty("任务ID")
    private String taskId;

    @ApiModelProperty("业务ID")
    private String businessId;

}
