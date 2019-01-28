package com.aispread.manager.flowable.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * APP端任务列表请求条件
 * Created by Vim 2019/1/11 19:59
 *
 * @author Vim
 */
@Data
public class FlowAppTaskInfoQuery implements Serializable {

    /**
     * 是否为手机端
     */
    @ApiModelProperty(value = "是否为手机端")
    private boolean mobile;

}
