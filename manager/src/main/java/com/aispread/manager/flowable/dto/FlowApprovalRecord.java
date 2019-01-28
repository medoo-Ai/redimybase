package com.aispread.manager.flowable.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 流程审批记录
 * Created by Vim 2019/1/11 16:10
 *
 * @author Vim
 */
@Data
public class FlowApprovalRecord implements Serializable {


    /**
     * 发送时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;


    /**
     * 完成时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;


    /**
     * 耗时
     */
    private String duration;

    /**
     * 审批人
     */
    private String assignee;

    /**
     * 审批动作
     */
    private String action;

    /**
     * 意见
     */
    private String comment;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务CODE
     */
    private String taskCode;
}
