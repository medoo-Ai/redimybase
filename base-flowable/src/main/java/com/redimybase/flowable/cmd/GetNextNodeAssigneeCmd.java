package com.redimybase.flowable.cmd;

import com.aispread.manager.flowable.dto.FlowNextNodeInfo;
import com.aispread.manager.flowable.entity.FlowNodeEntity;
import com.aispread.manager.flowable.entity.FlowUserEntity;
import com.aispread.manager.flowable.service.FlowNodeService;
import com.aispread.manager.flowable.service.FlowUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.listener.SpringContextListener;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.*;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取下一节点审批人CMD
 * Created by Vim 2019/1/10 10:26
 *
 * @author Vim
 */
public class GetNextNodeAssigneeCmd implements Command<FlowNextNodeInfo> {
    @Override
    public FlowNextNodeInfo execute(CommandContext commandContext) {
        //FlowNodeEntity currentNodeEntity = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("task_code", this.currentActivityId).eq("flow_definition_id", this.processDefinitionId).select("id"));

        ProcessEngineConfiguration engineConfiguration = (ProcessEngineConfiguration) commandContext.getCurrentEngineConfiguration();
        //取到对应的流程模型
        BpmnModel bpmnModel = engineConfiguration.getRepositoryService().getBpmnModel(this.processDefinitionId);

        //取到模型下的流程节点
        FlowElement currentElement = bpmnModel.getFlowElement(this.currentActivityId);

        List<List<String>> nodeInfoList = getNextNodeDefinitionId(currentElement);
        //下一节点的定义ID
        List<String> nextNodeDefinitionId = new ArrayList<>(nodeInfoList.get(0));

        if (nextNodeDefinitionId.size() == 0) {
            return null;
        }

        //找到下一节点对应的节点ID
        List<FlowNodeEntity> nextNodeEntities = flowNodeService.list(new QueryWrapper<FlowNodeEntity>().in("task_code", nextNodeDefinitionId).eq("flow_definition_id", this.processDefinitionId).select("id"));
        List<String> nextNodeIdList = new ArrayList<>();
        for (FlowNodeEntity nextNodeEntity : nextNodeEntities) {
            nextNodeIdList.add(nextNodeEntity.getId());
        }

        List<FlowUserEntity> userEntities = flowUserService.list(new QueryWrapper<FlowUserEntity>().in("node_id", nextNodeIdList).select("value,type"));

        List<String> userIdList = new ArrayList<>();
        for (FlowUserEntity userEntity : userEntities) {
            if (FlowUserEntity.Type.USER == userEntity.getType() ||
                    FlowUserEntity.Type.INITIATOR == userEntity.getType() ||
                    FlowUserEntity.Type.LEADERSHIP == userEntity.getType()) {
                userIdList.add(userEntity.getValue());
            }
        }


        FlowNextNodeInfo nodeInfo = new FlowNextNodeInfo();

        if (userIdList.size() > 0) {
            List<UserEntity> userList = userService.list(new QueryWrapper<UserEntity>().in("id", userIdList).select("user_name"));
            StringBuilder nextUserId = new StringBuilder();

            for (int i = 0; i < userList.size(); i++) {
                if (i == userList.size() - 1) {
                    nextUserId.append(userList.get(i).getUserName());
                }else{
                    nextUserId.append(userList.get(i).getUserName()).append(",");
                }
            }
            nodeInfo.setNextAssignee(nextUserId.toString());
        }

        nodeInfo.setNextNodeName(StringUtils.join(nodeInfoList.get(1), ","));
        return nodeInfo;
    }

    /**
     * 获取下一节点的定义ID
     */
    private List<List<String>> getNextNodeDefinitionId(FlowElement currentElement) {
        List<List<String>> data = new ArrayList<>();
        data.add(new ArrayList<>());
        data.add(new ArrayList<>());
        //工作流节定义ID
        List<String> nodeDefinitionIdList = new ArrayList<>();

        //工作流流程节点名称
        List<String> nodeNameList = new ArrayList<>();
        if (currentElement instanceof UserTask) {
            //如果当前节点是用户任务
            UserTask currentTask = (UserTask) currentElement;
            //取到当前任务的输出连线
            List<SequenceFlow> outgoingFlows = currentTask.getOutgoingFlows();

            getNodeInfoList(nodeDefinitionIdList, nodeNameList, outgoingFlows);

        }

        if (currentElement instanceof StartEvent) {
            //如果当前节点是开始节点
            StartEvent startEvent = (StartEvent) currentElement;
            //取到当前任务的输出连线
            List<SequenceFlow> outgoingFlows = startEvent.getOutgoingFlows();

            getNodeInfoList(nodeDefinitionIdList, nodeNameList, outgoingFlows);
        }
        data.get(0).addAll(nodeDefinitionIdList);
        data.get(1).addAll(nodeNameList);

        return data;
    }

    private void getNodeInfoList(List<String> nodeDefinitionIdList, List<String> nodeNameList, List<SequenceFlow> outgoingFlows) {
        for (SequenceFlow outgoingFlow : outgoingFlows) {
            FlowElement outgoingElement = outgoingFlow.getTargetFlowElement();
            if (outgoingElement instanceof UserTask) {
                //如果输出连线指向的是用户任务
                nodeDefinitionIdList.add(outgoingElement.getId());
                nodeNameList.add(outgoingElement.getName());
            }
            if (outgoingElement instanceof SubProcess) {
                for (FlowElement flowElement : ((SubProcess) outgoingElement).getFlowElements()) {
                    List<List<String>> tmpList = getNextNodeDefinitionId(flowElement);
                    nodeDefinitionIdList.addAll(tmpList.get(0));
                    nodeNameList.addAll(tmpList.get(1));
                }
            }
            if (outgoingElement instanceof SequenceFlow) {
                List<List<String>> tmpList = getNextNodeDefinitionId(((SequenceFlow) outgoingElement).getTargetFlowElement());
                nodeDefinitionIdList.addAll(tmpList.get(0));
                nodeNameList.addAll(tmpList.get(1));
            }
            if (outgoingElement instanceof ExclusiveGateway) {
                getNodeInfoList(nodeDefinitionIdList, nodeNameList, ((ExclusiveGateway) outgoingElement).getOutgoingFlows());
            }
        }
    }

    public GetNextNodeAssigneeCmd(String currentActivityId, String processDefinitionId) {
        this.currentActivityId = currentActivityId;
        this.processDefinitionId = processDefinitionId;
        initService();
    }

    private void initService() {
        flowNodeService = SpringContextListener.getBean(FlowNodeService.class);
        flowUserService = SpringContextListener.getBean(FlowUserService.class);
        userService = SpringContextListener.getBean(UserService.class);
    }

    /**
     * 当前流程节点CODE
     */
    private String currentActivityId;

    /**
     * 工作流流程定义ID
     */
    private String processDefinitionId;


    private FlowNodeService flowNodeService;


    private FlowUserService flowUserService;

    private UserService userService;


}
