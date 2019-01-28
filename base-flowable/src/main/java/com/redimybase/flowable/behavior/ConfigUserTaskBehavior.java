package com.redimybase.flowable.behavior;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.flowable.cmd.GetFirstNodeCmd;
import com.redimybase.flowable.listener.task.contants.FlowVariableConstants;
import com.redimybase.framework.listener.SpringContextListener;
import com.aispread.manager.flowable.entity.*;
import com.aispread.manager.flowable.service.*;
import com.redimybase.manager.security.entity.PositionEntity;
import com.redimybase.manager.security.entity.UserOrgEntity;
import com.redimybase.manager.security.entity.UserPositionEntity;
import com.redimybase.manager.security.entity.UserRoleEntity;
import com.redimybase.manager.security.service.PositionService;
import com.redimybase.manager.security.service.UserOrgService;
import com.redimybase.manager.security.service.UserPositionService;
import com.redimybase.manager.security.service.UserRoleService;
import com.redimybase.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.common.impl.el.ExpressionManager;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.service.TaskService;
import org.flowable.task.service.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.flowable.engine.impl.util.CommandContextUtil.*;

/**
 * 任务节点创建前配置流程信息
 * Created by Vim 2018/11/19 16:39
 *
 * @author Vim
 */
@Slf4j
public class ConfigUserTaskBehavior extends UserTaskActivityBehavior {

    public ConfigUserTaskBehavior(UserTask userTask) {
        super(userTask);

        flowDefinitionService = SpringContextListener.getBean(FlowDefinitionService.class);
        flowNodeService = SpringContextListener.getBean(FlowNodeService.class);
        flowRuUserService = SpringContextListener.getBean(FlowRuUserService.class);
        userRoleService = SpringContextListener.getBean(UserRoleService.class);
        positionService = SpringContextListener.getBean(PositionService.class);
        flowRuFormService = SpringContextListener.getBean(FlowRuFormService.class);
        actHiVarinstService = SpringContextListener.getBean(ActHiVarinstService.class);
        flowAgentService = SpringContextListener.getBean(FlowAgentService.class);

    }

    @Override
    public void execute(DelegateExecution execution) {
        super.execute(execution);
    }

    @Override
    protected void handleAssignments(TaskService taskService, String assignee, String owner, List<String> candidateUsers, List<String> candidateGroups, TaskEntity task, ExpressionManager expressionManager, DelegateExecution execution) {

        String processDefId = task.getProcessDefinitionId();
        this.taskCode = task.getTaskDefinitionKey();
        this.definitionId = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("flow_definition_id", processDefId).select("id")).getId();
        this.nodeId = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("definition_id", this.definitionId).eq("task_code", this.taskCode).select("id")).getId();

        this.businessId = (String) task.getVariable(FlowVariableConstants.业务单号);

        if (this.businessId == null) {
            log.error("流程启动配置错误,flow_definition_id:{}", this.definitionId);
            return;
        }

        //配置表单
        configForm(task);

        //配置用户
        configUser(task);


        //configVar(task);

        super.handleAssignments(taskService, assignee, owner, candidateUsers, candidateGroups, task, expressionManager, execution);
    }


    /**
     * 配置审批人用户
     */
    private void configUser(TaskEntity task) {
        List<FlowRuUserEntity> flowUserEntities = flowRuUserService.list(new QueryWrapper<FlowRuUserEntity>().eq("business_id", this.businessId).eq("task_code", this.taskCode));

        //任务对应的历史流程实例
        HistoricProcessInstanceEntity processInstanceEntity = getProcessEngineConfiguration().getHistoricProcessInstanceEntityManager().findById(task.getProcessInstanceId());

        //流程发起人ID
        String startUserId = processInstanceEntity.getStartUserId();
        FlowAgentEntity flowAgentEntity;
        for (FlowRuUserEntity flowUserEntity : flowUserEntities) {
            switch (flowUserEntity.getType()) {
                case FlowUserEntity.Type.INITIATOR:
                    task.setAssignee(startUserId);
                    break;
                case FlowUserEntity.Type.USER:
                    flowAgentEntity = flowAgentService.findAgent(flowUserEntity.getValue(), task.getProcessDefinitionId());
                    if (null != flowAgentEntity) {
                        //代理设置
                        task.setAssignee(flowAgentEntity.getAgentId());
                    } else {
                        task.setAssignee(flowUserEntity.getValue());
                    }
                    break;
                case FlowUserEntity.Type.USER_GROUP:
                    List<UserRoleEntity> userRoleEntities = userRoleService.list(new QueryWrapper<UserRoleEntity>().eq("role_id", flowUserEntity.getValue()).select("user_id"));
                    for (UserRoleEntity userRoleEntity : userRoleEntities) {
                        flowAgentEntity = flowAgentService.findAgent(flowUserEntity.getValue(), task.getProcessDefinitionId());
                        if (null != flowAgentEntity) {
                            //代理设置
                            task.addCandidateUser(flowAgentEntity.getAgentId());
                        } else {
                            task.addCandidateUser(userRoleEntity.getUserId());
                        }
                    }
                    break;
                case FlowUserEntity.Type.ORTHER_NODE:
                case FlowUserEntity.Type.FROM_FORM:
                    //来自节点或表单用户
                    task.addCandidateUser(flowUserEntity.getValue());
                    break;

                case FlowUserEntity.Type.LEADERSHIP:
                    //上级领导
                    if (null != startUserId) {
                        String leadId = positionService.getLeadershipId(startUserId);
                        flowAgentEntity = flowAgentService.findAgent(leadId, task.getProcessDefinitionId());
                        if (null != flowAgentEntity) {
                            //代理设置
                            task.setAssignee(flowAgentEntity.getAgentId());
                        } else {
                            task.setAssignee(leadId);
                        }
                    } else {
                        task.setAssignee(SecurityUtil.getCurrentUserId());
                    }
                    break;
                default:
                    break;
            }


        }
    }

    /**
     * 配置流程变量
     */
    private void configVar(TaskEntity task) {

        FlowElement flowElement = getProcessEngineConfiguration().getManagementService().executeCommand(new GetFirstNodeCmd(task.getProcessDefinitionId(), true));
        if (StringUtils.equals(task.getTaskDefinitionKey(), flowElement.getId())) {
            //如果是首个节点
            List<ActHiVarinstEntity> actHiVarinstEntities = actHiVarinstService.list(new QueryWrapper<ActHiVarinstEntity>().eq("PROC_INST_ID_", task.getProcessInstanceId()).eq("TASK_ID_", null));

            //删除全局的流程变量
            getHistoricVariableService().deleteHistoricVariableInstancesByProcessInstanceId(task.getProcessInstanceId());

            Map<String, Object> variables = new HashMap<>();

            for (ActHiVarinstEntity actHiVarinstEntity : actHiVarinstEntities) {
                variables.put(actHiVarinstEntity.getName(), actHiVarinstEntity.getText());
            }
            task.setVariablesLocal(variables);
        }
    }


    /**
     * 配置流程表单
     */
    private void configForm(TaskEntity task) {
        FlowRuFormEntity formKey = flowRuFormService.getOne(new QueryWrapper<FlowRuFormEntity>().eq("business_id", this.businessId).eq("task_code", this.taskCode).select("id"));
        if (null != formKey) {
            task.setFormKey(formKey.getId());
        }
    }

    /**
     * 流程定义ID
     */
    private String definitionId;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 流程节点CODE
     */
    private String taskCode;

    /**
     * 业务ID
     */
    private String businessId;
    private FlowDefinitionService flowDefinitionService;
    private FlowNodeService flowNodeService;
    private FlowRuUserService flowRuUserService;
    private FlowRuFormService flowRuFormService;
    private UserRoleService userRoleService;
    private PositionService positionService;
    private ActHiVarinstService actHiVarinstService;

    private FlowAgentService flowAgentService;

}
