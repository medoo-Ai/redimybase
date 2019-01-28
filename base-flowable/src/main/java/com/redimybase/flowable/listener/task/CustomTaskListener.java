package com.redimybase.flowable.listener.task;

import com.redimybase.flowable.bean.CustomFlow;
import com.redimybase.framework.listener.SpringContextListener;
import com.aispread.manager.flowable.service.FlowDefinitionService;
import com.aispread.manager.flowable.service.FlowNodeService;
import com.aispread.manager.flowable.service.FlowUserService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * 自定义流程监听器(监听流程启动时被调用)
 * Created by Vim 2018/11/17 14:31
 *
 * @author Vim
 */
@Slf4j
public abstract class CustomTaskListener implements TaskListener {



    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        try {
            CustomFlow customFlow = new CustomFlow();

            /*customFlow.setFlowStatus((Integer) delegateTask.getVariable(FlowVariableConstants.审批状态));
            customFlow.setVariables(delegateTask.getVariables());*/

            if (TaskListener.EVENTNAME_CREATE.equals(eventName)) {
                this.onCreate(delegateTask, customFlow);
            }

            if (TaskListener.EVENTNAME_ASSIGNMENT.equals(eventName)) {
                this.onAssignment(delegateTask, customFlow);
            }

            if (TaskListener.EVENTNAME_COMPLETE.equals(eventName)) {
                this.onComplete(delegateTask, customFlow);
            }

            if (TaskListener.EVENTNAME_DELETE.equals(eventName)) {
                this.onComplete(delegateTask, customFlow);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    public void onCreate(DelegateTask task, CustomFlow customFlow) throws Exception {

    }

    public void onAssignment(DelegateTask task, CustomFlow customFlow) throws Exception {

    }

    public void onComplete(DelegateTask task, CustomFlow customFlow) throws Exception {

    }

    public void onDelete(DelegateTask task, CustomFlow customFlow) throws Exception {

    }

    public CustomTaskListener() {
        flowNodeService = SpringContextListener.getBean(FlowNodeService.class);
        flowUserService = SpringContextListener.getBean(FlowUserService.class);
        flowDefinitionService = SpringContextListener.getBean(FlowDefinitionService.class);
    }

    protected FlowNodeService flowNodeService;

    protected FlowUserService flowUserService;

    protected FlowDefinitionService flowDefinitionService;

}
