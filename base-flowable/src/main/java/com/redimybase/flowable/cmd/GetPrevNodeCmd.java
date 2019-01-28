package com.redimybase.flowable.cmd;

import com.redimybase.framework.exception.BusinessException;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.impl.context.Context;

/**
 * 获取上一个节点
 * Created by Vim 2019/1/11 15:42
 *
 * @author Vim
 */
public class GetPrevNodeCmd implements Command<FlowElement> {

    @Override
    public FlowElement execute(CommandContext commandContext) {

        BpmnModel bpmnModel = Context.getProcessEngineConfiguration().getRepositoryService().getBpmnModel(this.processDefinitionId);

        FlowElement currentElement = bpmnModel.getFlowElement(this.currentActivityId);

        if (!(currentElement instanceof UserTask)) {
            throw new BusinessException(500, "获取上一个节点出错,节点类型不是UserTask");
        }

        UserTask currentTask = (UserTask) currentElement;

        return null;
    }

    public GetPrevNodeCmd(String currentActivityId, String processDefinitionId) {
        this.currentActivityId = currentActivityId;
        this.processDefinitionId = processDefinitionId;
    }

    /**
     * 当前流程节点定义ID
     */
    private String currentActivityId;
    /**
     * 工作流流程定义ID
     */
    private String processDefinitionId;
}
