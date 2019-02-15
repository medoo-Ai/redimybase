package org.flowable.validation.validator.impl;

import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.validation.ValidationError;
import org.flowable.validation.validator.ProcessLevelValidator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 流转串行网关验证器
 * Created by Vim 2019/1/29 14:25
 *
 * @author Vim
 */
public class ExclusiveGatewayValidator extends ProcessLevelValidator {
    public ExclusiveGatewayValidator() {
    }

    protected void executeValidation(BpmnModel bpmnModel, Process process, List<ValidationError> errors) {
        List<ExclusiveGateway> gateways = process.findFlowElementsOfType(ExclusiveGateway.class);
        Iterator var5 = gateways.iterator();

        while(var5.hasNext()) {
            ExclusiveGateway gateway = (ExclusiveGateway)var5.next();
            this.validateExclusiveGateway(process, gateway, errors);
        }

    }

    public void validateExclusiveGateway(Process process, ExclusiveGateway exclusiveGateway, List<ValidationError> errors) {
        if (exclusiveGateway.getOutgoingFlows().isEmpty()) {
            this.addError(errors, "flowable-exclusive-gateway-no-outgoing-seq-flow", process, exclusiveGateway, "流转串行网关没有目标流程线.");
        } else if (exclusiveGateway.getOutgoingFlows().size() == 1) {
            SequenceFlow sequenceFlow = (SequenceFlow)exclusiveGateway.getOutgoingFlows().get(0);
            if (StringUtils.isNotEmpty(sequenceFlow.getConditionExpression())) {
                this.addError(errors, "flowable-exclusive-gateway-condition-not-allowed-on-single-seq-flow", process, exclusiveGateway, "流转串行网关z只有一条目标流程线,不允许设置条件");
            }
        } else {
            String defaultSequenceFlow = exclusiveGateway.getDefaultFlow();
            List<SequenceFlow> flowsWithoutCondition = new ArrayList();
            Iterator var6 = exclusiveGateway.getOutgoingFlows().iterator();

            while(var6.hasNext()) {
                SequenceFlow flow = (SequenceFlow)var6.next();
                String condition = flow.getConditionExpression();
                boolean isDefaultFlow = flow.getId() != null && flow.getId().equals(defaultSequenceFlow);
                boolean hasCondition = StringUtils.isNotEmpty(condition);
                if (!hasCondition && !isDefaultFlow) {
                    flowsWithoutCondition.add(flow);
                }

                if (hasCondition && isDefaultFlow) {
                    this.addError(errors, "flowable-exclusive-gateway-condition-on-seq-flow", process, exclusiveGateway, "默认的流程线条件不符合规则");
                }
            }

            if (!flowsWithoutCondition.isEmpty()) {
                this.addWarning(errors, "flowable-exclusive-gateway-seq-flow-without-conditions", process, exclusiveGateway, "流转串行网关只有一条目标流程线有条件(不是默认的)");
            }
        }

    }
}
