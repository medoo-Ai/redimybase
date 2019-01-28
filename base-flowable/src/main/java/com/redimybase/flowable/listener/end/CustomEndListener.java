package com.redimybase.flowable.listener.end;

import com.redimybase.flowable.bean.CustomFlow;
import com.redimybase.flowable.listener.business.FlowBusinessListener;
import com.redimybase.flowable.listener.task.contants.FlowVariableConstants;
import com.redimybase.framework.listener.SpringContextListener;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.TaskService;
import org.flowable.engine.common.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.common.api.delegate.event.FlowableEvent;
import org.flowable.engine.common.api.delegate.event.FlowableEventListener;
import org.flowable.engine.common.impl.event.FlowableEntityEventImpl;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

import java.util.List;
import java.util.Map;

/**
 * 自定义结束事件
 * Created by Vim 2019/1/26 19:33
 *
 * @author Vim
 */
@Slf4j
public class CustomEndListener implements ExecutionListener {


    @Override
    public void notify(DelegateExecution execution) {
        if ("end".equals(execution.getEventName())) {
            callListener(execution);
        }
    }

    /**
     * 调用业务监听器
     */
    private void callListener(DelegateExecution execution) {
        if (businessListeners == null) {
            businessListeners = (List<FlowBusinessListener>) SpringContextListener.getBean("flowBusinessListeners");
        }
        if (businessListeners == null) {
            log.error("流程业务监听器失效,{}", String.format("[eventType:%s]", execution.getId()));
            return;
        }
        if (taskService == null) {
            taskService = SpringContextListener.getBean(TaskService.class);
        }
        for (FlowBusinessListener listener : businessListeners) {
            CustomFlow customFlow = new CustomFlow();
            Task task = taskService.createTaskQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
            Map<String, Object> variables = task.getProcessVariables();

            Integer status = (Integer) variables.get(FlowVariableConstants.审批状态);

            customFlow.setFlowStatus(status);
            customFlow.setTaskType((String) variables.get(FlowVariableConstants.业务类型));
            customFlow.setVariables(variables);
            if (FlowVariableConstants.TaskStatus.已通过.equals(status)) {
                listener.onPass(customFlow);
            } else if (FlowVariableConstants.TaskStatus.已拒绝.equals(status)) {
                listener.onReject(customFlow);
            }
        }
    }


    private List<FlowBusinessListener> businessListeners;

    private TaskService taskService;

}
