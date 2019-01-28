package com.aispread.manager.flowable.dto;

import com.aispread.manager.flowable.entity.ActHiProcinstEntity;
import com.aispread.manager.flowable.entity.FlowButtonEntity;
import com.aispread.manager.flowable.entity.FlowDistributeTaskEntity;
import com.aispread.manager.flowable.entity.FlowRuButtonEntity;
import com.aispread.manager.form.entity.FormFieldEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

/**
 * 审批流程视图DTO
 * Created by Vim 2019/1/9 14:26
 *
 * @author Vim
 */
@Data
public class FlowApprovalViewDTO implements Serializable {

    /**
     * 表单JSON
     */
    private String formJson;

    /**
     * 启动表单JSON
     */
    private String startFormJson;

    /**
     * 表单字段集合
     */
    private List<FormFieldEntity> formFields;

    /**
     * 字段值
     */
    private List<ActHiProcinstEntity> fieldValues;

    /**
     * 操作按钮集合
     */
    private List<FlowRuButtonEntity> buttons;

    /**
     * 发起人
     */
    private String startUser;

    /**
     * 流程发起时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 审批人
     */
    private String assignee;

    /**
     * 下一节点审批人
     */
    private String nextNodeAssignee;

    /**
     * 下一节点名称
     */
    private String nextNodeName;

    /**
     * 业务数据ID
     */
    private String businessId;

    /**
     * 表单ID
     */
    private String runFormId;


    /**
     * 审批记录
     */
    private List<FlowApprovalRecord> approvalRecords;

    /**
     * 表单名称
     */
    private String formName;

    /**
     * 是否可编辑(0:否,1:是)
     */
    private Boolean edit;

    /**
     * 任务节点CODE
     */
    private String taskCode;

    /**
     * 分发任务
     */
    private List<FlowDistributeRecord> distributeTaskRecords;

}
