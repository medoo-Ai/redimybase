package com.redimybase.flowable.behavior;

import com.redimybase.flowable.bean.CustomFlow;
import com.redimybase.flowable.listener.business.FlowBusinessListener;
import com.redimybase.flowable.listener.task.contants.FlowVariableConstants;
import com.redimybase.framework.listener.SpringContextListener;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior;

import java.util.List;
import java.util.Map;

/**
 * Created by Vim 2019/1/27 10:18
 *
 * @author Vim
 */
@Slf4j
public class EndEventBehavior extends NoneEndEventActivityBehavior {

    @Override
    public void execute(DelegateExecution execution) {
        if (!(execution.getCurrentFlowElement().getParentContainer() instanceof Process)) {
            return;
        }
        callListener(execution);
        super.execute(execution);
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
            Map<String, Object> variables = execution.getVariables();

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
