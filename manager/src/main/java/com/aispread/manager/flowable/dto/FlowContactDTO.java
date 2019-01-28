package com.aispread.manager.flowable.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 流程沟通DTO
 * Created by Vim 2019/1/9 17:53
 *
 * @author Vim
 */
@Data
@ApiModel(value = "流程沟通model")
public class FlowContactDTO implements Serializable {

    /**
     * 接收人
     */
    @ApiModelProperty(value = "接收人")
    private String toUserId;

    /**
     * 接收组
     */
    @ApiModelProperty(value = "接收组")
    private String toOrgId;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;


}
