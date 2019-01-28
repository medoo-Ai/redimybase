package com.redimybase.flowable.cmd;

import com.aispread.manager.flowable.entity.ActRuVariableEntity;
import com.aispread.manager.flowable.service.ActRuVariableService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.listener.SpringContextListener;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.flowable.engine.impl.util.CommandContextUtil.getHistoricVariableService;
import static org.flowable.engine.impl.util.CommandContextUtil.getVariableService;

/**
 * 设置启动表单到首个节点CMD
 * Created by Vim 2019/1/10 15:08
 *
 * @author Vim
 */
public class SetStartFormCmd  implements Command<String> {
    @Override
    public String execute(CommandContext commandContext) {
        ProcessEngineConfiguration configuration = (ProcessEngineConfiguration) commandContext.getCurrentEngineConfiguration();
        TaskEntity task = (TaskEntity) configuration.getTaskService().createTaskQuery().processInstanceId(this.processInstanceId).singleResult();
        List<ActRuVariableEntity> actRuVariableEntities = actRuVariableService.list(new QueryWrapper<ActRuVariableEntity>().eq("PROC_INST_ID_", task.getProcessInstanceId()).isNull("TASK_ID_"));
        if (actRuVariableEntities.size() == 0) {
            return null;
        }
        //删除启动表单的流程变量
        getHistoricVariableService().deleteHistoricVariableInstancesByProcessInstanceId(task.getProcessInstanceId());
        getVariableService().deleteVariablesByExecutionId(task.getProcessInstanceId());

        Map<String, Object> variables = new HashMap<>();

        for (ActRuVariableEntity actRuVariableEntity : actRuVariableEntities) {
            variables.put(actRuVariableEntity.getName(), actRuVariableEntity.getText());
        }
        task.setVariablesLocal(variables);
        return task.getId();
    }

    public SetStartFormCmd(String startActivityId, String processInstanceId, String processDefinitionId) {
        this.startActivityId = startActivityId;
        this.processInstanceId = processInstanceId;
        this.processDefinitionId = processDefinitionId;

        actRuVariableService = SpringContextListener.getBean(ActRuVariableService.class);
    }

    /**
     * 工作流第一个节点定义ID
     */
    private String startActivityId;

    /**
     * 工作流流程实例ID
     */
    private String processInstanceId;

    /**
     * 工作流流程定义ID
     */
    private String processDefinitionId;


    private ActRuVariableService actRuVariableService;


}
