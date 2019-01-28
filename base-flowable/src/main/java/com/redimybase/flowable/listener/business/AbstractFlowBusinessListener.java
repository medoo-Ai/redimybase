package com.redimybase.flowable.listener.business;

import com.redimybase.flowable.bean.CustomFlow;
import lombok.extern.slf4j.Slf4j;

/**
 * 流程业务监听器
 * <p>例如监听审批事件,同意或不同意</p>
 * Created by Vim 2019/1/26 21:26
 *
 * @author Vim
 */
@Slf4j
public abstract class AbstractFlowBusinessListener implements FlowBusinessListener {

    @Override
    public void onPass(CustomFlow customFlow) {
        log.debug("流程通过 -> {}", customFlow.getTaskType());
    }

    @Override
    public void onReject(CustomFlow customFlow) {
        log.debug("流程驳回 -> {}", customFlow.getTaskType());
    }
}
