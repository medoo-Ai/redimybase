package com.redimybase.flowable.cmd;

import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;

/**
 * Created by Vim 2019/1/10 12:04
 *
 * @author Vim
 */
public class GetNextNodeNameCmd implements Command<String> {

    @Override
    public String execute(CommandContext commandContext) {
        return null;
    }

    /**
     * 当前工作流节点ID
     */
    private String currentActivityId;


}
