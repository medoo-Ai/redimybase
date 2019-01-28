package com.redimybase.flowable.cmd;

import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;

import java.util.List;

/**
 * 获取流程第一个节点CMD
 * Created by Vim 2019/1/9 15:55
 *
 * @author Vim
 */
public class GetFirstNodeCmd implements Command<FlowElement> {
    @Override
    public FlowElement execute(CommandContext commandContext) {
        ProcessEngineConfiguration processEngineConfiguration = (ProcessEngineConfiguration) commandContext.getCurrentEngineConfiguration();
        RepositoryService repositoryService = processEngineConfiguration.getRepositoryService();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        Process process = bpmnModel.getProcesses().get(0);

        StartEvent startEvent = null;
        for (FlowElement flowElement : process.getFlowElements()) {
            if (flowElement instanceof StartEvent) {
                startEvent = (StartEvent) flowElement;
                break;
            }
        }

        if (null != startEvent) {
            if (!this.completeFirstTask) {
                return startEvent;
            }
            List<SequenceFlow> tempOutgoingFlows = startEvent.getOutgoingFlows();
            if (tempOutgoingFlows != null && tempOutgoingFlows.size() > 0) {
                return startEvent.getOutgoingFlows().get(0).getTargetFlowElement();
            }else{
                return startEvent;
            }

        }
        return null;
    }

    public GetFirstNodeCmd(String processDefinitionId, boolean completeFirstTask) {
        this.processDefinitionId = processDefinitionId;
        this.completeFirstTask = completeFirstTask;
    }

    /**
     * 流程定义ID
     */
    private String processDefinitionId;

    /**
     * 是否完成首个任务
     */
    private boolean completeFirstTask;
}
