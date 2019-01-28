package com.aispread.admin.notice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 发送消息请求
 * Created by Vim 2019/1/27 1 *
 * @author Vim
 */
@Data
@ApiModel("发送消息MODEL")
public class SendMessageRequest implements Serializable {

    @ApiModelProperty("登录平台(pc:PC端,app:手机端)")
    private String os;
    @ApiModelProperty("消息类型")
    private String type;

    @ApiModelProperty("消息内容")
    private String message;

    @ApiModelProperty("目标用户ID list")
    private List<String> targetList;
}
