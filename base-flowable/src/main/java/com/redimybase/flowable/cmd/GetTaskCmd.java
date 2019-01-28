package com.redimybase.flowable.cmd;

import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.task.api.Task;


/**
 * 获取全局流程变量
 * Created by Vim 2019/1/26 21:58
 *
 * @author Vim
 */
public class GetTaskCmd implements Command<Task> {
    @Override
    public Task execute(CommandContext commandContext) {
        ProcessEngineConfiguration processEngineConfiguration = (ProcessEngineConfiguration) commandContext.getCurrentEngineConfiguration();
        return processEngineConfiguration.getTaskService().createTaskQuery().processInstanceId(this.processInstanceId).singleResult();
    }

    public GetTaskCmd(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    /**
     * 流程实例ID
     */
    private String processInstanceId;
}
