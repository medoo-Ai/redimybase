package com.redimybase.flowable.service;

import com.aispread.manager.flowable.dto.*;
import com.aispread.manager.flowable.entity.FlowDefinitionEntity;
import com.aispread.manager.flowable.entity.FlowDistributeTaskEntity;
import com.aispread.manager.flowable.entity.FlowRuFormEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.manager.security.entity.UserEntity;
import org.flowable.engine.runtime.ProcessInstance;

import java.util.Map;

/**
 * 流程操作相关
 * Created by Vim 2018/10/12 0012 21:52
 */
public interface ProcessHandleService {

    /**
     * 获取所有流程实例
     */
    public Page<?> listInstance(Page page);


    /**
     * 获取所有待办任务
     */
    public Page<?> listTodoTask(Page page);

    /**
     * 获取所有流程定义
     */
    public Page<?> listDefinitions(Page page);


    /**
     * 获取所有代理中的任务
     */
    public Page<?> listAgentTask(String userId, Page page);

    /**
     * 获取所有已部署的流程
     */
    public Page<?> listDeployed(Page page);


    /**
     * 转办任务
     *
     * @param taskId 任务ID
     * @param userId 转办用户ID
     */
    public void transfer(String taskId, String userId);


    /**
     * 与任务审批人沟通
     *
     * @param taskId     任务ID
     * @param userId     审批人ID
     * @param noticeType 沟通类型
     */
    public void linkup(String taskId, String userId, String noticeType);

    /**
     * 终止任务
     *
     * @param taskId 任务ID
     */
    public void stopTask(String taskId);

    /**
     * 恢复任务
     *
     * @param taskId 任务ID
     */
    public void recoveryTask(String taskId);

    /**
     * 任务自由跳转
     *
     * @param taskId          任务ID
     * @param targetElementId 目标节点ID
     */
    public void jumpTask(String taskId, String targetElementId);


    /**
     * 显示审批视图(当前/历史节点的视图都可以查看)
     */
    public FlowApprovalViewDTO approvalView(FlowApprovalViewQuery query);

    /**
     * 显示发起流程视图
     *
     * @param flowDefinitionId 流程定义ID
     */
    public FlowStartFlowViewDTO startFlowView(String flowDefinitionId);


    /**
     * 已办任务视图显示
     */
    public FlowDoneViewDTO doneTaskView(FlowDoneViewQuery query);

    /**
     * 已申请视图显示
     */
    public FlowApplyViewDTO applyTaskView(FlowApplyViewQuery query);


    /**
     * 流程沟通
     */
    public void contact(FlowContactDTO contactDTO);


    /**
     * 获取历史任务变量
     */
    public void getHisTaskVar(String taskId);

    /**
     * 发起流程
     *
     * @param currentUser         当前用户
     * @param businessId          业务数据ID
     * @param processDefinitionId 流程定义ID
     * @param variablesJson       流程变量JSON
     * @param businessType        业务类型
     * @return 流程实例对象
     */
    public ProcessInstance startProcessInstance(UserEntity currentUser, String businessId, String processDefinitionId, String variablesJson, String businessType);

    /**
     * 完成任务
     */
    public void complete(FlowCompleteDTO completeDTO);


    /**
     * 保存流程运行数据
     */
    public void saveRuData(String businessId, String processDefinitionId, Map<String, Object> varFields);


    public FlowRuFormEntity viewTaskForm(String businessId, String taskCode);


    /**
     * 驳回
     */
    public void reject(FlowRejectDTO flowRejectDTO);


    /**
     * 分发任务
     */
    public void distribute(FlowDistributeRequest request);


    /**
     * 反馈任务
     */
    public void feedback(FlowDistributeTaskEntity entity);

    /**
     * 验证流程定义配置
     */
    public void validateDefinition(FlowDefinitionEntity entity);

}

