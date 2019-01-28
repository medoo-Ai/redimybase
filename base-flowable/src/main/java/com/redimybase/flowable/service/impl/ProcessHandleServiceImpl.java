package com.redimybase.flowable.service.impl;

import com.aispread.manager.flowable.dto.*;
import com.aispread.manager.flowable.entity.*;
import com.aispread.manager.flowable.service.*;
import com.aispread.manager.form.dto.FormField;
import com.aispread.manager.form.entity.FormEntity;
import com.aispread.manager.form.entity.FormFieldEntity;
import com.aispread.manager.form.service.FormFieldService;
import com.aispread.manager.form.service.FormService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.flowable.cmd.GetFirstNodeCmd;
import com.redimybase.flowable.cmd.GetNextNodeAssigneeCmd;
import com.redimybase.flowable.cmd.JumpTaskCmd;
import com.redimybase.flowable.listener.task.contants.FlowVariableConstants;
import com.redimybase.flowable.service.ProcessHandleService;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.service.UserService;
import com.redimybase.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.ManagementService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.common.impl.identity.Authentication;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Vim 2018/10/12 0012 22:20
 */
@Service
@Slf4j
public class ProcessHandleServiceImpl implements ProcessHandleService {


    @Override
    public Page<?> listInstance(Page page) {
        long total = runtimeService.createProcessInstanceQuery().count();

        page.setTotal(total);
        page.setRecords(runtimeService.createProcessInstanceQuery().listPage((int) page.getCurrent(), (int) page.getSize()));
        return page;
    }

    @Override
    public Page<?> listTodoTask(Page page) {
        long total = taskService.createTaskQuery().count();

        page.setTotal(total);
        page.setRecords(taskService.createTaskQuery().listPage((int) page.getCurrent(), (int) page.getSize()));
        return page;
    }

    @Override
    public Page<?> listDefinitions(Page page) {
        long total = repositoryService.createProcessDefinitionQuery().count();

        page.setTotal(total);
        page.setRecords(repositoryService.createProcessDefinitionQuery().listPage((int) page.getCurrent(), (int) page.getSize()));
        return page;
    }

    @Override
    public Page<?> listAgentTask(String userId, Page page) {
        long total = taskService.createTaskQuery().taskOwner(userId).taskDelegationState(DelegationState.PENDING).count();

        page.setRecords(taskService.createTaskQuery().taskOwner(userId).taskDelegationState(DelegationState.PENDING).listPage((int) page.getCurrent(), (int) page.getSize()));
        page.setTotal(total);
        return page;
    }

    @Override
    public Page<?> listDeployed(Page page) {
        long total = repositoryService.createDeploymentQuery().count();

        page.setTotal(total);
        page.setRecords(repositoryService.createDeploymentQuery().listPage((int) page.getCurrent(), (int) page.getSize()));
        return page;
    }

    @Override
    public void transfer(String taskId, String userId) {
        log.debug("[转办任务] - taskId:{},userId:{}", taskId, userId);
        taskService.setAssignee(taskId, userId);
    }

    @Override
    public void linkup(String taskId, String userId, String noticeType) {
        //TODO 任务监控沟通
    }

    @Override
    public void stopTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        runtimeService.suspendProcessInstanceById(task.getProcessInstanceId());
    }

    @Override
    public void recoveryTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        runtimeService.activateProcessInstanceById(task.getProcessInstanceId());
    }


    @Override
    public void jumpTask(String taskId, String targetElementId) {
        managementService.executeCommand(new JumpTaskCmd(targetElementId, taskId, "自由跳转"));
    }


    @Override
    public FlowApprovalViewDTO approvalView(FlowApprovalViewQuery query) {
        String taskId = query.getTaskId();
        String businessId = query.getBusinessId();
        String userId = SecurityUtil.getCurrentUserId();

        FlowApprovalViewDTO dto = new FlowApprovalViewDTO();

        //获取到对应的流程任务
        ActRuTaskEntity actRuTaskEntity = actRuTaskService.getOne(new QueryWrapper<ActRuTaskEntity>().eq("ID_", taskId).select("PROC_DEF_ID_,TASK_DEF_KEY_,TASK_DEF_ID_,PROC_INST_ID_,ASSIGNEE_"));

        //任务节点CODE
        dto.setTaskCode(actRuTaskEntity.getTaskDefKey());

        ActHiProcinstEntity actHiProcinstEntity = actHiProcinstService.getOne(new QueryWrapper<ActHiProcinstEntity>().eq("BUSINESS_KEY_", businessId).select("START_TIME_,START_USER_ID_"));

        if (actHiProcinstEntity != null) {
            //流程发起时间
            dto.setStartTime(actHiProcinstEntity.getStartTime());
        }

        List<FlowRuButtonEntity> flowRuButtonEntities = flowRuButtonService.list(new QueryWrapper<FlowRuButtonEntity>().eq("task_code", actRuTaskEntity.getTaskDefKey()).eq("business_id", businessId).orderByDesc("sort"));

        //取到对应的流程定义
        FlowDefinitionEntity definitionEntity = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("flow_definition_id", actRuTaskEntity.getProcDefId()).select("id,flow_definition_id"));

        //获取对应的流程节点
        FlowNodeEntity nodeEntity = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("task_code", actRuTaskEntity.getTaskDefKey()).eq("definition_id", definitionEntity.getId()).select("id,initiator_view,assignee_view"));

        //流程首个节点
        FlowElement firstElement = managementService.executeCommand(new GetFirstNodeCmd(actRuTaskEntity.getProcDefId(), true));

        if (StringUtils.equals(actRuTaskEntity.getTaskDefKey(), firstElement.getId()) && StringUtils.equals(actRuTaskEntity.getAssignee(), userId)) {
            //如果当前节点是首个节点并且审批人是本人则可编辑可撤回
            dto.setEdit(true);
        } else {
            dto.setEdit(false);
        }
        FlowRuFormEntity firstRuFormEntity = flowRuFormService.getOne(new QueryWrapper<FlowRuFormEntity>().eq("business_id", businessId).eq("task_code", firstElement.getId()).select("json,form_name"));
        if (null != firstRuFormEntity) {
            //启动表单JSON
            dto.setStartFormJson(firstRuFormEntity.getJson());

            //运行表单名称
            dto.setFormName(firstRuFormEntity.getFormName());
        }


        FlowNextNodeInfo nodeInfo = managementService.executeCommand(new GetNextNodeAssigneeCmd(actRuTaskEntity.getTaskDefKey(), definitionEntity.getFlowDefinitionId()));
        if (null != nodeInfo) {
            //下一节点审批人
            dto.setNextNodeAssignee(nodeInfo.getNextAssignee());

            //下一节点名称
            dto.setNextNodeName(nodeInfo.getNextNodeName());
        }


        if (nodeEntity.getInitiatorView() == 1) {
            //发起人可见
            UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("id", actHiProcinstEntity.getStartUserId()).select("user_name"));
            dto.setStartUser(userEntity.getUserName());
        }

        if (nodeEntity.getInitiatorView() == 1) {
            //审批人可见
            if (StringUtils.isNotEmpty(actRuTaskEntity.getAssignee())) {
                UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("id", actRuTaskEntity.getAssignee()));
                dto.setAssignee(userEntity.getUserName());
            }
        }

        //业务ID
        dto.setBusinessId(businessId);

        //审批记录
        dto.setApprovalRecords(actHiTaskinstService.approvalRecord(actRuTaskEntity.getProcInstId()));

        //操作按钮
        List<FlowRuButtonEntity> buttons = new ArrayList<>();

        //当前节点是否为首个节点
        boolean firstNodeFlag = StringUtils.equals(actRuTaskEntity.getTaskDefKey(), firstElement.getId());
        //当前节点审批人是否为当前用户
        boolean nodeAssigneeFlag = StringUtils.equals(actRuTaskEntity.getAssignee(), userId);
        for (FlowRuButtonEntity flowRuButtonEntity : flowRuButtonEntities) {
            if ((FlowButtonEntity.Type.同意.equals(flowRuButtonEntity.getType()) ||
                    FlowButtonEntity.Type.驳回.equals(flowRuButtonEntity.getType()))) {
                if (nodeAssigneeFlag) {
                    buttons.add(flowRuButtonEntity);
                }
            }
            if (FlowButtonEntity.Type.撤回.equals(flowRuButtonEntity.getType()) || FlowButtonEntity.Type.提交.equals(flowRuButtonEntity.getType())) {
                if (firstNodeFlag && nodeAssigneeFlag) {
                    buttons.add(flowRuButtonEntity);
                }
            }
        }

        //分发任务记录
        dto.setDistributeTaskRecords(flowDistributeTaskService.getRecord(businessId, actRuTaskEntity.getTaskDefKey()));

        //当前节点是否为一个分发任务节点
        if (flowDistributeTaskService.count(
                new QueryWrapper<FlowDistributeTaskEntity>()
                        .eq("business_id", businessId)
                        .eq("task_code", actRuTaskEntity.getTaskDefKey())
                        .eq("status", FlowDistributeTaskEntity.Status.待回馈)
                        .eq("assignee", userId)
        ) > 0) {
            FlowRuButtonEntity flowRuButtonEntity = new FlowRuButtonEntity();
            flowRuButtonEntity.setType(FlowButtonEntity.Type.反馈);
            buttons.add(flowRuButtonEntity);
        }

        //分发按钮
        if (StringUtils.equals(userId, actRuTaskEntity.getAssignee())) {
            FlowRuButtonEntity distributeButton = new FlowRuButtonEntity();
            distributeButton.setType(FlowButtonEntity.Type.分发);
            buttons.add(distributeButton);
        }

        dto.setButtons(buttons);


        return dto;
    }

    @Override
    public FlowDoneViewDTO doneTaskView(FlowDoneViewQuery query) {
        String taskId = query.getTaskId();
        String businessId = query.getBusinessId();
        String userId = SecurityUtil.getCurrentUserId();

        FlowDoneViewDTO dto = new FlowDoneViewDTO();

        ActHiTaskinstEntity actHiTaskinstEntity = actHiTaskinstService.getOne(new QueryWrapper<ActHiTaskinstEntity>().eq("ID_", taskId).select("TASK_DEF_KEY_,PROC_DEF_ID_,PROC_INST_ID_"));

        ActHiProcinstEntity actHiProcinstEntity = actHiProcinstService.getOne(new QueryWrapper<ActHiProcinstEntity>().eq("BUSINESS_KEY_", businessId).select("START_TIME_,START_USER_ID_"));

        if (actHiProcinstEntity != null) {
            //流程发起时间
            dto.setStartTime(actHiProcinstEntity.getStartTime());
        }

        //分发任务记录
        dto.setDistributeTaskRecords(flowDistributeTaskService.getRecord(businessId, actHiTaskinstEntity.getTaskDefKey()));


        //取到对应的流程定义
        FlowDefinitionEntity definitionEntity = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("flow_definition_id", actHiTaskinstEntity.getProcDefId()).select("id,flow_definition_id"));

        //获取目前流程流转到的节点
        ActRuTaskEntity actRuTaskEntity = actRuTaskService.getOne(new QueryWrapper<ActRuTaskEntity>().eq("PROC_INST_ID_", actHiTaskinstEntity.getProcInstId()).select("TASK_DEF_KEY_"));
        FlowNextNodeInfo nodeInfo = managementService.executeCommand(new GetNextNodeAssigneeCmd(actRuTaskEntity.getTaskDefKey(), definitionEntity.getFlowDefinitionId()));
        if (null != nodeInfo) {
            //下一节点审批人
            dto.setNextNodeAssignee(nodeInfo.getNextAssignee());

            //下一节点名称
            dto.setNextNodeName(nodeInfo.getNextNodeName());
        }
        //获取对应的流程节点
        FlowNodeEntity nodeEntity = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("task_code", actHiTaskinstEntity.getTaskDefKey()).eq("definition_id", definitionEntity.getId()).select("id,initiator_view,assignee_view"));

        //流程首个节点
        FlowElement firstElement = managementService.executeCommand(new GetFirstNodeCmd(actHiTaskinstEntity.getProcDefId(), true));

        if (StringUtils.equals(actHiTaskinstEntity.getTaskDefKey(), firstElement.getId()) && StringUtils.equals(actHiTaskinstEntity.getAssignee(), userId)) {
            //如果当前节点是首个节点并且审批人是本人则可编辑可撤回
            dto.setEdit(true);
        } else {
            dto.setEdit(false);
        }
        FlowRuFormEntity firstRuFormEntity = flowRuFormService.getOne(new QueryWrapper<FlowRuFormEntity>().eq("business_id", businessId).eq("task_code", firstElement.getId()).select("json,form_name"));
        if (null != firstRuFormEntity) {
            //启动表单JSON
            dto.setStartFormJson(firstRuFormEntity.getJson());

            //运行表单名称
            dto.setFormName(firstRuFormEntity.getFormName());
        }


        if (nodeEntity.getInitiatorView() == 1) {
            //发起人可见
            UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("id", actHiProcinstEntity.getStartUserId()).select("user_name"));
            dto.setStartUser(userEntity.getUserName());
        }

        if (nodeEntity.getInitiatorView() == 1) {
            //审批人可见
            if (StringUtils.isNotEmpty(actHiTaskinstEntity.getAssignee())) {
                UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("id", actHiTaskinstEntity.getAssignee()));
                dto.setAssignee(userEntity.getUserName());
            }
        }


        //业务ID
        dto.setBusinessId(businessId);

        //审批记录
        dto.setApprovalRecords(actHiTaskinstService.approvalRecord(actHiTaskinstEntity.getProcInstId()));

        //不可编辑
        dto.setEdit(false);

        return dto;
    }

    @Override
    public FlowApplyViewDTO applyTaskView(FlowApplyViewQuery query) {
        String taskId = query.getTaskId();
        String businessId = query.getBusinessId();
        String userId = SecurityUtil.getCurrentUserId();

        FlowApplyViewDTO dto = new FlowApplyViewDTO();

        ActHiTaskinstEntity actHiTaskinstEntity = actHiTaskinstService.getOne(new QueryWrapper<ActHiTaskinstEntity>().eq("ID_", query.getTaskId()).select("TASK_DEF_KEY_,PROC_INST_ID_,PROC_DEF_ID_"));

        ActHiProcinstEntity actHiProcinstEntity = actHiProcinstService.getOne(new QueryWrapper<ActHiProcinstEntity>().eq("BUSINESS_KEY_", businessId).select("START_TIME_,START_USER_ID_"));

        if (actHiProcinstEntity != null) {
            //流程发起时间
            dto.setStartTime(actHiProcinstEntity.getStartTime());
        }


        List<FlowRuButtonEntity> flowRuButtonEntities = flowRuButtonService.list(new QueryWrapper<FlowRuButtonEntity>().eq("task_code", actHiTaskinstEntity.getTaskDefKey()).eq("business_id", businessId));


        //取到对应的流程定义
        FlowDefinitionEntity definitionEntity = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("flow_definition_id", actHiTaskinstEntity.getProcDefId()).select("id,flow_definition_id"));

        //获取目前流程流转到的节点
        ActRuTaskEntity actRuTaskEntity = actRuTaskService.getOne(new QueryWrapper<ActRuTaskEntity>().eq("PROC_INST_ID_", actHiTaskinstEntity.getProcInstId()).select("TASK_DEF_KEY_"));
        FlowNextNodeInfo nodeInfo = managementService.executeCommand(new GetNextNodeAssigneeCmd(actRuTaskEntity.getTaskDefKey(), definitionEntity.getFlowDefinitionId()));
        if (null != nodeInfo) {
            //下一节点审批人
            dto.setNextNodeAssignee(nodeInfo.getNextAssignee());

            //下一节点名称
            dto.setNextNodeName(nodeInfo.getNextNodeName());
        }
        //获取对应的流程节点
        FlowNodeEntity nodeEntity = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("task_code", actHiTaskinstEntity.getTaskDefKey()).eq("definition_id", definitionEntity.getId()).select("id,initiator_view,assignee_view"));

        //流程首个节点
        FlowElement firstElement = managementService.executeCommand(new GetFirstNodeCmd(actHiTaskinstEntity.getProcDefId(), true));

        if (StringUtils.equals(actHiTaskinstEntity.getTaskDefKey(), firstElement.getId()) && StringUtils.equals(actHiTaskinstEntity.getAssignee(), userId)) {
            //如果当前节点是首个节点并且审批人是本人则可编辑可撤回
            dto.setEdit(true);
        }
        FlowRuFormEntity firstRuFormEntity = flowRuFormService.getOne(new QueryWrapper<FlowRuFormEntity>().eq("business_id", businessId).eq("task_code", firstElement.getId()).select("json,form_name"));
        if (null != firstRuFormEntity) {
            //启动表单JSON
            dto.setStartFormJson(firstRuFormEntity.getJson());

            //运行表单名称
            dto.setFormName(firstRuFormEntity.getFormName());
        }


        if (nodeEntity.getInitiatorView() == 1) {
            //发起人可见
            UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("id", actHiProcinstEntity.getStartUserId()).select("user_name"));
            dto.setStartUser(userEntity.getUserName());
        }

        if (nodeEntity.getInitiatorView() == 1) {
            //审批人可见
            if (StringUtils.isNotEmpty(actHiTaskinstEntity.getAssignee())) {
                UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("id", actHiTaskinstEntity.getAssignee()));
                dto.setAssignee(userEntity.getUserName());
            }
        }


        //业务ID
        dto.setBusinessId(businessId);

        //审批记录
        dto.setApprovalRecords(actHiTaskinstService.approvalRecord(actHiTaskinstEntity.getProcInstId()));

        //不可编辑
        dto.setEdit(false);

        List<FlowRuButtonEntity> buttons = new ArrayList<>();
        for (FlowRuButtonEntity flowRuButtonEntity : flowRuButtonEntities) {
            if (FlowButtonEntity.Type.撤回.equals(flowRuButtonEntity.getType())) {
                buttons.add(flowRuButtonEntity);
            }
        }

        dto.setButtons(buttons);

        return dto;
    }

    @Override
    public FlowStartFlowViewDTO startFlowView(String flowDefinitionId) {
        FlowStartFlowViewDTO dto = new FlowStartFlowViewDTO();

        FlowDefinitionEntity definitionEntity = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("id", flowDefinitionId).select("flow_definition_id,complete_first_task"));


        //取流程第一个节点
        FlowElement flowElement = managementService.executeCommand(new GetFirstNodeCmd(definitionEntity.getFlowDefinitionId(), true));

        //根据节点CODE和流程定义ID找到对应的流程节点
        FlowNodeEntity nodeId = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("task_code", flowElement.getId()).eq("definition_id", flowDefinitionId).select("id"));

        //找到对应节点的表单KEY
        FlowFormEntity flowFormKey = flowFormService.getOne(new QueryWrapper<FlowFormEntity>().eq("node_id", nodeId.getId()).select("form_key"));

        if (null != flowFormKey) {
            FormEntity formEntity = formService.getOne(new QueryWrapper<FormEntity>().eq("form_key", flowFormKey.getFormKey()).select("id,json"));
            if (null != formEntity) {
                //表单JSON
                dto.setFormJson(formEntity.getJson());

                //表单ID
                //dto.setFormId(formEntity.getId());
            }
        }
        //操作按钮
        dto.setButtons(flowButtonService.list(new QueryWrapper<FlowButtonEntity>().eq("node_id", nodeId.getId())));

        FlowNextNodeInfo nodeInfo = managementService.executeCommand(new GetNextNodeAssigneeCmd(flowElement.getId(), definitionEntity.getFlowDefinitionId()));
        if (null != nodeInfo) {
            //下一节点审批人
            dto.setNextNodeAssignee(nodeInfo.getNextAssignee());

            //下一节点名称
            dto.setNextNodeName(nodeInfo.getNextNodeName());
        }
        return dto;
    }


    @Override
    public void contact(FlowContactDTO contactDTO) {
        //TODO 发送沟通通知(邮件,站内通知等)
        String[] toUserIdArray = StringUtils.split(contactDTO.getToUserId(), ",");
        String[] toOrgIdArray = StringUtils.split(contactDTO.getToOrgId(), ",");
        Set<String> toUserSet = new HashSet<>(Arrays.asList(toUserIdArray));
        toUserSet.addAll(userService.getUserIdByOrgId(contactDTO.getToOrgId(), UserEntity.Status.ENABLE));


    }

    @Override
    public void getHisTaskVar(String taskId) {

    }

    @Override
    public ProcessInstance startProcessInstance(UserEntity currentUser, String businessId, String processDefinitionId, String variablesJson, String businessType) {
        Authentication.setAuthenticatedUserId(currentUser.getId());

        //流程变量
        Map<String, Object> formFields = JSON.parseObject(variablesJson, Map.class);

        formFields.put(FlowVariableConstants.审批状态, FlowVariableConstants.TaskStatus.审批中);
        formFields.put(FlowVariableConstants.业务类型, businessType);
        formFields.put(FlowVariableConstants.业务单号, businessId);

        //保存运行数据
        saveRuData(businessId, processDefinitionId, formFields);

        ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.startProcessInstanceById(processDefinitionId, businessId, formFields);

        //获取流程第一个节点
        FlowElement flowElement = managementService.executeCommand(new GetFirstNodeCmd(processDefinitionId, true));

        String formId = null;
        FlowDefinitionEntity flowDefinitionEntity = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("flow_definition_id", processDefinitionId).select("id"));
        //根据节点CODE和流程定义ID找到对应的流程节点
        FlowNodeEntity nodeId = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("task_code", flowElement.getId()).eq("definition_id", flowDefinitionEntity.getId()).select("id"));

        //找到对应节点的表单KEY
        FlowFormEntity flowFormKey = flowFormService.getOne(new QueryWrapper<FlowFormEntity>().eq("node_id", nodeId.getId()).select("form_key"));

        if (null != flowFormKey) {
            FormEntity formEntity = formService.getOne(new QueryWrapper<FormEntity>().eq("form_key", flowFormKey.getFormKey()).select("id,json"));
            if (null != formEntity) {
                formId = formEntity.getId();
            }
        }
        for (Map.Entry<String, Object> entry : formFields.entrySet()) {
            FormFieldEntity formFieldEntity = formFieldService.getOne(new QueryWrapper<FormFieldEntity>().eq("form_id", formId).eq("field_name", entry.getKey()).select("type"));
            if (null != formFieldEntity) {
                if ("attachment".equals(formFieldEntity.getType())) {
                    saveFlowAttachment(businessId, String.valueOf(entry.getValue()), flowElement.getId());
                }
            }
        }

        return executionEntity;
    }


    @Override
    public void complete(FlowCompleteDTO completeDTO) {
        String currentUserId = SecurityUtil.getCurrentUserId();

        //本地表单变量
        Map<String, Object> formFields = JSON.parseObject(completeDTO.getVariables(), Map.class);
        //全局流程变量
        Map<String, Object> variables = new HashMap<>();

        ActRuTaskEntity actRuTaskEntity = actRuTaskService.getOne(new QueryWrapper<ActRuTaskEntity>().eq("ID_", completeDTO.getTaskId()).select("PROC_INST_ID_,TASK_DEF_KEY_"));

        //审批动作
        String reason = null;
        //流程审批状态
        Integer status = null;
        FlowRuNodeEntity flowRuNodeEntity = flowRuNodeService.getOne(new QueryWrapper<FlowRuNodeEntity>().eq("business_id", completeDTO.getBusinessId()).eq("task_code", actRuTaskEntity.getTaskDefKey()).select("type"));
        if (flowRuNodeEntity != null) {
            if (completeDTO.getAction() == 1) {
                reason = "同意";
                status = FlowVariableConstants.TaskStatus.已通过;
            } else if (completeDTO.getAction() == 0) {
                reason = "不同意";
                status = FlowVariableConstants.TaskStatus.已拒绝;
            } else {
                reason = "提交";
            }
        }


        FlowRuFormEntity flowRuFormEntity = flowRuFormService.getOne(new QueryWrapper<FlowRuFormEntity>().eq("id", completeDTO.getRunFormId()).select("json"));
        if (null != flowRuFormEntity) {
            List<FormField> runFormFields = JSONArray.parseArray(flowRuFormEntity.getJson(), FormField.class);

            Map<String, Object> attachmentFields = new HashMap<>();

            //找到运行表单中为附件类型的组件
            for (FormField runFormField : runFormFields) {
                if ("attachment".equals(runFormField.getType())) {
                    attachmentFields.put(runFormField.getFieldName(), runFormField.getValue());
                }
            }

            for (Map.Entry<String, Object> entry : formFields.entrySet()) {
                if (null != attachmentFields.get(entry.getKey())) {
                    saveFlowAttachment(completeDTO.getBusinessId(), String.valueOf(entry.getValue()), completeDTO.getTaskId());
                }
            }
        }


        //将审批动作加入流程变量
        variables.put(FlowVariableConstants.审批状态, status);
        taskService.setVariables(completeDTO.getTaskId(), variables);

        taskService.setVariablesLocal(completeDTO.getTaskId(), formFields);

        if (completeDTO.getAction() == 1) {
            taskService.claim(completeDTO.getTaskId(), currentUserId);
            taskService.complete(completeDTO.getTaskId());
        } else {
            Page<ActHiTaskinstEntity> page = new Page<>();
            page.setCurrent(1).setSize(2);
            IPage<ActHiTaskinstEntity> pageRecord = actHiTaskinstService.page(page, new QueryWrapper<ActHiTaskinstEntity>().eq("PROC_INST_ID_", actRuTaskEntity.getProcInstId()).orderByDesc("START_TIME_"));
            if (pageRecord.getTotal() == 0) {
                throw new BusinessException(500, "未找到上一步审批节点");
            }
            ActHiTaskinstEntity prevHiTaskinstEntity = pageRecord.getRecords().get(1);
            managementService.executeCommand(new JumpTaskCmd(prevHiTaskinstEntity.getTaskDefId(), completeDTO.getTaskId(), reason));
        }

        //保存运行表单
        flowRuFormService.saveRuForm(completeDTO.getBusinessId(), completeDTO.getRunFormId(), formFields);

        FlowOpinionEntity flowOpinionEntity = new FlowOpinionEntity();
        flowOpinionEntity.setTaskId(completeDTO.getTaskId());
        flowOpinionEntity.setContent(completeDTO.getComment());
        flowOpinionEntity.setCreateTime(new Date());
        flowOpinionEntity.setAction(reason);
        flowOpinionEntity.setTaskCode(actRuTaskEntity.getTaskDefKey());
        flowOpinionEntity.setAttachmentId(completeDTO.getAttachmentId());
        flowOpinionService.save(flowOpinionEntity);
    }

    @Override
    public void saveRuData(String businessId, String processDefinitionId, Map<String, Object> varFields) {
        FlowDefinitionEntity flowDefinitionEntity = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("flow_definition_id", processDefinitionId).select("id"));

        List<FlowNodeEntity> flowNodeEntities = flowNodeService.list(new QueryWrapper<FlowNodeEntity>().eq("definition_id", flowDefinitionEntity.getId()).select("id,task_code,type"));

        for (FlowNodeEntity flowNodeEntity : flowNodeEntities) {

            //保存流程运行表单
            FlowRuFormEntity ruFormEntity = new FlowRuFormEntity();
            ruFormEntity.setBusinessId(businessId);
            ruFormEntity.setCreateTime(new Date());
            ruFormEntity.setTaskCode(flowNodeEntity.getTaskCode());
            FormEntity formEntity = formService.getFormByNodeId(flowNodeEntity.getId());
            if (null != formEntity) {
                //给表单JSON设置流程变量值
                if (null != formEntity.getJson() && varFields.size() > 0) {
                    List<FormField> formFields = JSONArray.parseArray(formEntity.getJson(), FormField.class);

                    for (FormField formField : formFields) {
                        Object fieldValue = varFields.get(formField.getFieldName());
                        formField.setValue(fieldValue != null ? String.valueOf(fieldValue) : null);
                    }
                    ruFormEntity.setFormName(formEntity.getName());
                    ruFormEntity.setJson(JSONArray.toJSONString(formFields));
                }
                flowRuFormService.save(ruFormEntity);
            }

            //保存流程运行节点
            FlowRuNodeEntity flowRuNodeEntity = new FlowRuNodeEntity();
            flowRuNodeEntity.setBusinessId(businessId);
            flowRuNodeEntity.setTaskCode(flowNodeEntity.getTaskCode());
            flowRuNodeEntity.setType(flowNodeEntity.getType());
            flowRuNodeService.save(flowRuNodeEntity);


            //保存流程运行按钮
            List<FlowButtonEntity> flowButtonEntities = flowButtonService.list(new QueryWrapper<FlowButtonEntity>().eq("node_id", flowNodeEntity.getId()));
            for (FlowButtonEntity flowButtonEntity : flowButtonEntities) {
                FlowRuButtonEntity buttonEntity = new FlowRuButtonEntity();
                buttonEntity.setBusinessId(businessId);
                buttonEntity.setCreateTime(new Date());
                buttonEntity.setSort(flowButtonEntity.getSort());
                buttonEntity.setTaskCode(flowNodeEntity.getTaskCode());
                buttonEntity.setType(flowButtonEntity.getType());

                flowRuButtonService.save(buttonEntity);
            }

            //保存流程运行变量
            List<FlowVarEntity> flowVarEntities = flowVarService.list(new QueryWrapper<FlowVarEntity>().eq("node_id", flowNodeEntity.getId()));
            for (FlowVarEntity flowVarEntity : flowVarEntities) {
                FlowRuVarEntity flowRuVarEntity = new FlowRuVarEntity();
                flowRuVarEntity.setBusinessId(businessId);
                flowRuVarEntity.setCreateTime(new Date());
                flowRuVarEntity.setDefaultValue(flowVarEntity.getDefaultValue());
                flowRuVarEntity.setExpression(flowVarEntity.getExpression());
                flowRuVarEntity.setFormField(flowVarEntity.getFormField());
                flowRuVarEntity.setName(flowVarEntity.getName());
                flowRuVarEntity.setTaskCode(flowNodeEntity.getTaskCode());
                flowRuVarEntity.setSymbol(flowVarEntity.getSymbol());

                flowRuVarService.save(flowRuVarEntity);
            }

            //保存流程节点运行配置
            FlowNodeConfigEntity flowNodeConfigEntity = flowNodeConfigService.getOne(new QueryWrapper<FlowNodeConfigEntity>().eq("node_id", flowNodeEntity.getId()).select("reject_node_type"));
            if (flowNodeConfigEntity != null) {
                FlowRuNodeConfigEntity flowRuNodeConfigEntity = new FlowRuNodeConfigEntity();
                flowRuNodeConfigEntity.setBusinessId(businessId);
                flowRuNodeConfigEntity.setCreateTime(new Date());
                flowRuNodeConfigEntity.setRejectNodeType(flowNodeConfigEntity.getRejectNodeType());
                flowRuNodeConfigEntity.setTaskCode(flowNodeEntity.getTaskCode());
                flowRuNodeConfigService.save(flowRuNodeConfigEntity);
            }

            //保存流程运行用户
            FlowUserEntity flowUserEntity = flowUserService.getOne(new QueryWrapper<FlowUserEntity>().eq("node_id", flowNodeEntity.getId()).select("type,value"));
            if (flowUserEntity != null) {
                FlowRuUserEntity flowRuUserEntity = new FlowRuUserEntity();
                flowRuUserEntity.setType(flowUserEntity.getType());
                flowRuUserEntity.setBusinessId(businessId);
                flowRuUserEntity.setTaskCode(flowNodeEntity.getTaskCode());
                flowRuUserEntity.setValue(flowUserEntity.getValue());
                flowRuUserService.save(flowRuUserEntity);
            }
        }
    }

    @Override
    public FlowRuFormEntity viewTaskForm(String businessId, String taskCode) {
        return flowRuFormService.getOne(new QueryWrapper<FlowRuFormEntity>().eq("business_id", businessId).eq("task_code", taskCode).select("id,json"));
    }

    /**
     * 保存流程附件
     */
    private void saveFlowAttachment(String businessId, String attachmentId, String taskCode) {
        FlowBusinessAttachmentEntity attachmentEntity = new FlowBusinessAttachmentEntity();
        attachmentEntity.setBusinessId(businessId);
        attachmentEntity.setCreateTime(new Date());
        attachmentEntity.setTaskCode(taskCode);
        attachmentEntity.setAttachmentId(attachmentId);
        flowBusinessAttachmentService.save(attachmentEntity);
    }


    @Override
    public void reject(FlowRejectDTO flowRejectDTO) {
        String currentUserId = SecurityUtil.getCurrentUserId();
        //Authentication.setAuthenticatedUserId(currentUserId);

        ActRuTaskEntity actRuTaskEntity = actRuTaskService.getOne(new QueryWrapper<ActRuTaskEntity>().eq("ID_", flowRejectDTO.getTaskId()).select("TASK_DEF_ID_,PROC_INST_ID_,PROC_DEF_ID_,TASK_DEF_KEY_"));

        FlowOpinionEntity flowOpinionEntity = new FlowOpinionEntity();
        flowOpinionEntity.setAction("驳回");
        flowOpinionEntity.setCreateTime(new Date());
        flowOpinionEntity.setTaskCode(actRuTaskEntity.getTaskDefKey());
        flowOpinionEntity.setContent(flowRejectDTO.getComment());
        flowOpinionEntity.setTaskId(flowRejectDTO.getTaskId());
        flowOpinionEntity.setAttachmentId(flowRejectDTO.getAttachmentId());

        flowOpinionService.save(flowOpinionEntity);

        //运行流程节点配置
        FlowRuNodeConfigEntity ruNodeConfigEntity = flowRuNodeConfigService.getOne(new QueryWrapper<FlowRuNodeConfigEntity>().eq("task_code", actRuTaskEntity.getTaskDefKey()).eq("business_id", flowRejectDTO.getBusinessId()));

        //跳转的目标任务节点CODE
        String targetTaskCode = null;
        Integer rejectNodeType;
        if (ruNodeConfigEntity == null) {
            rejectNodeType = FlowNodeConfigEntity.Type.发起人;
        } else {
            rejectNodeType = ruNodeConfigEntity.getRejectNodeType();
        }

        if (FlowNodeConfigEntity.Type.直接结束.equals(rejectNodeType)) {
            targetTaskCode = flowRejectDTO.getTargetTaskCode();
        } else if (FlowNodeConfigEntity.Type.上一节点.equals(rejectNodeType)) {
            Page<ActHiTaskinstEntity> page = new Page<>();
            page.setCurrent(1).setSize(2);
            IPage<ActHiTaskinstEntity> pageRecord = actHiTaskinstService.page(page, new QueryWrapper<ActHiTaskinstEntity>().eq("PROC_INST_ID_", actRuTaskEntity.getProcInstId()).orderByDesc("startTime").select(""));
            ActHiTaskinstEntity actHiTaskinstEntity = pageRecord.getRecords().get(1);
            targetTaskCode = actHiTaskinstEntity.getTaskDefId();
        } else if (FlowNodeConfigEntity.Type.发起人.equals(rejectNodeType)) {
            FlowElement flowElement = managementService.executeCommand(new GetFirstNodeCmd(actRuTaskEntity.getProcDefId(), true));
            targetTaskCode = flowElement.getId();
        }
        if (null == targetTaskCode) {
            throw new BusinessException(500, "未找到节点");
        }

        managementService.executeCommand(new JumpTaskCmd(targetTaskCode, flowRejectDTO.getTaskId(), "驳回"));
    }

    @Override
    public void distribute(FlowDistributeRequest request) {
        FlowDistributeTaskEntity distributeTaskEntity = new FlowDistributeTaskEntity();
        distributeTaskEntity.setAssignee(request.getAssignee());
        distributeTaskEntity.setAttachmentId(request.getAttachmentId());
        distributeTaskEntity.setBusinessId(request.getBusinessId());
        distributeTaskEntity.setContent(request.getContent());
        distributeTaskEntity.setCreateTime(new Date());
        distributeTaskEntity.setStatus(FlowDistributeTaskEntity.Status.待回馈);
        distributeTaskEntity.setTaskCode(request.getTaskCode());
        distributeTaskEntity.setTitle(request.getTitle());
        distributeTaskEntity.setTaskId(request.getTaskId());

        flowDistributeTaskService.save(distributeTaskEntity);
    }

    @Override
    public void feedback(FlowDistributeTaskEntity entity) {
        entity.setStatus(FlowDistributeTaskEntity.Status.已回馈);
        flowDistributeTaskService.updateById(entity);
    }


    @Override
    public void validateDefinition(FlowDefinitionEntity entity) {
        FlowDefinitionEntity definitionEntity = flowDefinitionService.getById(entity.getId());

        List<FlowNodeEntity> flowNodeEntities = flowNodeService.list(new QueryWrapper<FlowNodeEntity>().eq("definition_id", definitionEntity.getId()));

        FlowElement firstElement = managementService.executeCommand(new GetFirstNodeCmd(definitionEntity.getFlowDefinitionId(), true));

        for (FlowNodeEntity node : flowNodeEntities) {
            if (FlowNodeEntity.Type.USER_TASK.equals(node.getType())) {
                //校验用户任务节点用户是否配全
                if (flowUserService.count(new QueryWrapper<FlowUserEntity>().eq("node_id", node.getId())) == 0) {
                    throw new BusinessException(201, String.format("[%s] 该节点审批人未配置,请重新配置", node.getName()));
                }
                //校验启动表单是否配置
                if (StringUtils.equals(node.getTaskCode(), firstElement.getId())) {
                    if (flowFormService.count(new QueryWrapper<FlowFormEntity>().eq("node_id", node.getId())) == 0) {
                        throw new BusinessException(201, String.format("[%s] 该节点的启动表单未配置,请重新配置", node.getName()));
                    }
                }
            }
            if (FlowNodeEntity.Type.EXCLUSIVE_GATEWAY.equals(node.getType())) {
                //校验流转网关变量条件是否配全

                //流程线集合
                List<FlowNodeEntity> sequenceList = flowNodeService.list(new QueryWrapper<FlowNodeEntity>().eq("parent_id", node.getId()).eq("type", FlowNodeEntity.Type.SEQUENCE_FLOW));

                List<String> sequenceIdList = new ArrayList<>();

                for (FlowNodeEntity sequence : sequenceList) {
                    sequenceIdList.add(sequence.getId());
                }

                //实际配置好的变量数量
                int varCount = flowVarService.count(new QueryWrapper<FlowVarEntity>().in("node_id", sequenceIdList));

                if (sequenceIdList.size() != varCount) {
                    throw new BusinessException(201, String.format("[%s] 该节点的流程变量未配全,请重新配置", node.getName()));
                }
            }


        }
    }

    @Autowired
    private FlowRuNodeService flowRuNodeService;

    @Autowired
    private FlowRuUserService flowRuUserService;

    @Autowired
    private FlowUserService flowUserService;

    @Autowired
    private FlowDistributeTaskService flowDistributeTaskService;

    @Autowired
    private FlowNodeConfigService flowNodeConfigService;

    @Autowired
    private ActHiProcinstService actHiProcinstService;

    @Autowired
    private FlowRuNodeConfigService flowRuNodeConfigService;

    @Autowired
    private FlowRuVarService flowRuVarService;

    @Autowired
    private FlowVarService flowVarService;

    @Autowired
    private FlowRuButtonService flowRuButtonService;


    @Autowired
    private FlowRuFormService flowRuFormService;


    @Autowired
    private ActHiTaskinstService actHiTaskinstService;

    @Autowired
    private FlowOpinionService flowOpinionService;

    @Autowired
    private UserService userService;


    @Autowired
    private ActRuTaskService actRuTaskService;

    @Autowired
    private FlowDefinitionService flowDefinitionService;

    @Autowired
    private FlowFormService flowFormService;

    @Autowired
    private FormService formService;

    @Autowired
    private FormFieldService formFieldService;

    @Autowired
    private FlowButtonService flowButtonService;

    @Autowired
    private FlowNodeService flowNodeService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private FlowBusinessAttachmentService flowBusinessAttachmentService;

}
