package com.redimybase.flowable.behavior;

import com.aispread.manager.flowable.entity.*;
import com.aispread.manager.flowable.service.*;
import com.aispread.manager.form.dto.FormField;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.flowable.cmd.GetFirstNodeCmd;
import com.redimybase.framework.listener.SpringContextListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.common.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.event.impl.FlowableEventBuilder;
import org.flowable.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.flowable.engine.impl.bpmn.helper.SkipExpressionUtil;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.condition.ConditionUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;

/**
 * 排他网关流程条件行为拓展
 * Created by Vim 2019/1/7 11:23
 *
 * @author Vim
 */
@Slf4j
public class ExclusiveGatewayConfigActivityBehavior extends ExclusiveGatewayActivityBehavior {

    @Override
    public void leave(DelegateExecution execution) {
        if (log.isDebugEnabled()) {
            log.debug("Leaving exclusive gateway '{}'", execution.getCurrentActivityId());
        }

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) execution.getCurrentFlowElement();

        if (CommandContextUtil.getProcessEngineConfiguration() != null && CommandContextUtil.getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
            CommandContextUtil.getProcessEngineConfiguration().getEventDispatcher().dispatchEvent(
                    FlowableEventBuilder.createActivityEvent(FlowableEngineEventType.ACTIVITY_COMPLETED, exclusiveGateway.getId(), exclusiveGateway.getName(), execution.getId(),
                            execution.getProcessInstanceId(), execution.getProcessDefinitionId(), exclusiveGateway));
        }

        SequenceFlow outgoingSequenceFlow = null;
        SequenceFlow defaultSequenceFlow = null;
        String defaultSequenceFlowId = exclusiveGateway.getDefaultFlow();

        // Determine sequence flow to take
        Iterator<SequenceFlow> sequenceFlowIterator = exclusiveGateway.getOutgoingFlows().iterator();
        while (outgoingSequenceFlow == null && sequenceFlowIterator.hasNext()) {
            SequenceFlow sequenceFlow = sequenceFlowIterator.next();

            String skipExpressionString = sequenceFlow.getSkipExpression();
            if (!SkipExpressionUtil.isSkipExpressionEnabled(execution, skipExpressionString)) {
                boolean conditionEvaluatesToTrue = ConditionUtil.hasTrueCondition(sequenceFlow, execution);
                if (conditionEvaluatesToTrue && (defaultSequenceFlowId == null || !defaultSequenceFlowId.equals(sequenceFlow.getId()))) {
                    if (log.isDebugEnabled()) {
                        log.debug("Sequence flow '{}' selected as outgoing sequence flow.", sequenceFlow.getId());
                    }
                    outgoingSequenceFlow = sequenceFlow;
                }
            } else if (SkipExpressionUtil.shouldSkipFlowElement(org.flowable.engine.common.impl.context.Context.getCommandContext(), execution, skipExpressionString)) {
                outgoingSequenceFlow = sequenceFlow;
            }

            // Already store it, if we would need it later. Saves one for loop.
            if (defaultSequenceFlowId != null && defaultSequenceFlowId.equals(sequenceFlow.getId())) {
                defaultSequenceFlow = sequenceFlow;
            }

        }


        //取首个节点
        FlowElement firstElement = Context.getProcessEngineConfiguration().getManagementService().executeCommand(new GetFirstNodeCmd(execution.getProcessDefinitionId(), true));

        FlowRuFormService flowRuFormService = SpringContextListener.getBean(FlowRuFormService.class);

        //首个节点的运行表单
        FlowRuFormEntity firstRuForm = flowRuFormService.getOne(new QueryWrapper<FlowRuFormEntity>().eq("business_id", execution.getProcessInstanceBusinessKey()).eq("task_code", firstElement.getId()).select("task_code,json"));


        if (null != firstRuForm) {

            List<FormField> runFormFields = JSONArray.parseArray(firstRuForm.getJson(), FormField.class);

            //首个表单运行变量
            Map<String, String> runVariables = new HashMap<>();
            for (FormField field : runFormFields) {
                runVariables.put(field.getFieldName(), field.getValue());
            }
            SequenceFlow evalSequence = null;

            if (outgoingSequenceFlow != null) {
                evalSequence = eval(execution, exclusiveGateway, outgoingSequenceFlow, runVariables);
            }
            if (evalSequence == null) {
                List<SequenceFlow> currentElementOutgoingFlows = ((ExclusiveGateway) execution.getCurrentFlowElement()).getOutgoingFlows();

                for (SequenceFlow currentElementOutgoingFlow : currentElementOutgoingFlows) {
                    //TODO 可能会出现outgoingSequenceFlow输出节点为null的情况,还待查原因,如果有任意一条线不设置变量会有选择问题
                    if (StringUtils.equals(outgoingSequenceFlow.getId(), currentElementOutgoingFlow.getId())) {
                        //如果是原有默认的流程线则跳过
                        continue;
                    }
                    evalSequence = eval(execution, exclusiveGateway, currentElementOutgoingFlow, runVariables);
                    if (null != evalSequence) {
                        outgoingSequenceFlow = evalSequence;
                    }
                }
            }
        }


        // Leave the gateway
        if (outgoingSequenceFlow != null) {
            execution.setCurrentFlowElement(outgoingSequenceFlow);
        } else {
            if (defaultSequenceFlow != null) {
                execution.setCurrentFlowElement(defaultSequenceFlow);
            } else {

                // No sequence flow could be found, not even a default one
                throw new FlowableException("No outgoing sequence flow of the exclusive gateway '" + exclusiveGateway.getId() + "' could be selected for continuing the process");
            }
        }

        bpmnActivityBehavior.performDefaultOutgoingBehavior((ExecutionEntity) execution);

    }

    private SequenceFlow eval(DelegateExecution execution, ExclusiveGateway exclusiveGateway, SequenceFlow currentElementOutgoingFlow, Map<String, String> runVariables) {

        FlowRuVarService flowRuVarService = SpringContextListener.getBean(FlowRuVarService.class);

        //获取运行流程变量
        List<FlowRuVarEntity> flowRuVarEntities = flowRuVarService.list(new QueryWrapper<FlowRuVarEntity>().eq("business_id", execution.getProcessInstanceBusinessKey()).eq("task_code", currentElementOutgoingFlow.getId()));

        if (flowRuVarEntities.size() > 0) {
            //表达式
            StringBuilder expression = new StringBuilder();
            //变量表达式
            //StringBuilder varExpression = new StringBuilder();
            for (int i = 0; i < flowRuVarEntities.size(); i++) {

                FlowRuVarEntity flowRuVarEntity = flowRuVarEntities.get(i);

                //赋值流程变量
                String variable = runVariables.get(flowRuVarEntity.getFormField());

                if (i == flowRuVarEntities.size() - 1) {
                    //如果是最后一个变量不加Symbol符号
                    if (StringUtils.isNotBlank(variable)) {
                        expression.append(flowRuVarEntity.getExpression().replace(flowRuVarEntity.getFormField(), "'" + variable + "'"));
                    } else {
                        expression.append(flowRuVarEntity.getExpression().replace(flowRuVarEntity.getFormField(), "'" + flowRuVarEntity.getDefaultValue() + "'"));
                    }
                } else {
                    if (StringUtils.isNotBlank(variable)) {
                        expression.append(flowRuVarEntity.getExpression().replace(flowRuVarEntity.getFormField(), variable)).append(toSymbol(flowRuVarEntity.getSymbol()));
                    } else {
                        expression.append(flowRuVarEntity.getExpression().replace(flowRuVarEntity.getFormField(), flowRuVarEntity.getDefaultValue())).append(toSymbol(flowRuVarEntity.getSymbol()));
                    }
                }
            }


            try {
                ScriptEngine scriptEngine = getScriptEngine("javascript");
                scriptEngine.eval(String.format(CARRIER, expression.toString()));
                boolean flag = (boolean) scriptEngine.get("a");
                if (flag) {
                    return currentElementOutgoingFlow;
                }
            } catch (ScriptException e) {
                log.error("流程节点计算条件表达式失败", e);
            }
        }
        return null;
    }


    private String toSymbol(Integer symbol) {
        if (FlowVarEntity.Symbol.并且.equals(symbol)) {
            return "&&";
        } else if (FlowVarEntity.Symbol.或者.equals(symbol)) {
            return "||";
        }
        return "";
    }


    private ScriptEngine getScriptEngine(String name) {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        return scriptEngineManager.getEngineByName(name);
    }

    public static void main(String[] args) throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("javascript");
        scriptEngine.eval(String.format(CARRIER, "1==2"));
        System.out.println(scriptEngine.get("a"));

    }


    public static final String CARRIER = "var a; if(%s){a=true;}else{a=false;}";

    public static final String VAR = "var %s=%s;";

}
