package com.aispread.admin.controller.document.listener;

import com.redimybase.flowable.bean.CustomFlow;
import com.redimybase.flowable.listener.business.AbstractFlowBusinessListener;
import java.util.Map;

/**
 * (案例)文档库流程监听器
 * Created by Vim 2019/1/26 21:24
 *
 * @author Vim
 */
public class FlowDocumentListener extends AbstractFlowBusinessListener {
    @Override
    public void onPass(CustomFlow customFlow) {
        if ("document".equals(customFlow.getTaskType())) {
            //TODO 通过流程类型判断是否为自己家的业务,如果是文档类型的流程,执行业务逻辑

            //流程状态
            Integer flowStatus = customFlow.getFlowStatus();

            //流程变量
            Map<String, Object> variables = customFlow.getVariables();
        }
    }

    @Override
    public void onReject(CustomFlow customFlow) {
        if ("document".equals(customFlow.getTaskType())) {
            //TODO 通过流程类型判断是否为自己家的业务,如果是文档类型的流程,执行业务逻辑

            //流程状态
            Integer flowStatus = customFlow.getFlowStatus();

            //流程变量
            Map<String, Object> variables = customFlow.getVariables();
        }
    }
}
