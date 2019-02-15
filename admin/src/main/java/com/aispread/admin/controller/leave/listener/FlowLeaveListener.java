package com.aispread.admin.controller.leave.listener;

import com.redimybase.flowable.bean.CustomFlow;
import com.redimybase.flowable.listener.business.AbstractFlowBusinessListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @auther SyntacticSugar
 * @data 2019/1/28 0028下午 5:42
 */
@Slf4j
public class FlowLeaveListener extends AbstractFlowBusinessListener {

    @Override
    public void onPass(CustomFlow customFlow) {
        if (customFlow.getTaskType().equals("事假")) {
        //todo  业务逻辑
            // 请假时间大于3天

            //流程状态 流程变量
            Integer flowStatus = customFlow.getFlowStatus();
            Map<String, Object> variables = customFlow.getVariables();
//            variables.put("user", )

        }
        log.debug("流程通过 -> {}", customFlow.getTaskType());
    }

    @Override
    public void onReject(CustomFlow customFlow) {
        if (customFlow.getTaskType().equals("事假")) {
            //todo  业务逻辑

            //流程状态 流程变量
            Integer flowStatus = customFlow.getFlowStatus();
            Map<String, Object> variables = customFlow.getVariables();


        }
        log.debug("流程驳回 -> {}", customFlow.getTaskType());
    }
}
