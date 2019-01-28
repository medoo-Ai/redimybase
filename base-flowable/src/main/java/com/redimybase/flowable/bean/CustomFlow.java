package com.redimybase.flowable.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;


/**
 * Created by Vim 2019/1/26 18:48
 *
 * @author Vim
 */
@Data
public class CustomFlow implements Serializable {

    /**
     * 审批状态,见com.redimybase.flowable.listener.task.contants.FlowVariableConstants.TaskStatus
     */
    private Integer flowStatus;

    /**
     * 流程变量
     */
    private Map<String, Object> variables;

    /**
     * 任务类型
     */
    private String taskType;



}
