package com.redimybase.flowable.cmd;

import com.aispread.manager.flowable.entity.*;
import com.aispread.manager.flowable.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.redimybase.framework.listener.SpringContextListener;
import com.aispread.manager.flowable.service.impl.FlowFormServiceImpl;
import com.aispread.manager.flowable.service.impl.FlowNodeServiceImpl;
import com.aispread.manager.flowable.service.impl.FlowUserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;

import java.util.Collection;

import java.util.Date;
import java.util.List;

/**
 * 同步更新流程定义
 * Created by Vim 2018/10/22 17:43
 */
public class SyncFlowCmd implements Command<Void> {

    public SyncFlowCmd(String processDefinitionId, String defId) {
        this.processDefinitionId = processDefinitionId;
        this.defId = defId;
        nodeService = SpringContextListener.getBean(FlowNodeServiceImpl.class);
        formService = SpringContextListener.getBean(FlowFormServiceImpl.class);
        userService = SpringContextListener.getBean(FlowUserServiceImpl.class);
        varService = SpringContextListener.getBean(FlowVarService.class);
        sequenceService = SpringContextListener.getBean(FlowSequenceService.class);
        buttonService = SpringContextListener.getBean(FlowButtonService.class);
        noticeService = SpringContextListener.getBean(FlowNoticeService.class);
        flowNodeConfigService = SpringContextListener.getBean(FlowNodeConfigService.class);
    }

    @Override
    public Void execute(CommandContext context) {
        ProcessEngineConfiguration processEngineConfiguration = (ProcessEngineConfiguration) context.getCurrentEngineConfiguration();

        //根据流程定义ID找到对应的流程模型
        BpmnModel model = processEngineConfiguration.getRepositoryService().getBpmnModel(processDefinitionId);

        Process process = model.getProcesses().get(0);

        //获取到模型里的流程节点
        Collection<FlowElement> flowElements = process.getFlowElements();

        //遍历前清空对应流程定义的节点信息
        beforeSaveNode();
        flowElements.forEach(element -> {
            saveNode(element, null);
        });
        return null;
    }

    /**
     * 保存节点前删除无用数据
     */
    private void beforeSaveNode() {
        List<FlowNodeEntity> list = nodeService.list(new QueryWrapper<FlowNodeEntity>().eq("definition_id", defId).select("id"));

        for (FlowNodeEntity node : list) {
            formService.remove(new QueryWrapper<FlowFormEntity>().eq("node_id", node.getId()));
            buttonService.remove(new QueryWrapper<FlowButtonEntity>().eq("node_id", node.getId()));
            noticeService.remove(new QueryWrapper<FlowNoticeEntity>().eq("node_id", node.getId()));
            sequenceService.remove(new QueryWrapper<FlowSequenceEntity>().eq("node_id", node.getId()));
            userService.remove(new QueryWrapper<FlowUserEntity>().eq("node_id", node.getId()));
            varService.remove(new QueryWrapper<FlowVarEntity>().eq("node_id", node.getId()));

            nodeService.removeById(node.getId());
        }
    }


    /**
     * 保存节点
     *
     * @param element 流程节点
     */
    private void saveNode(FlowElement element, String subProcessId) {
        if (element == null) {
            return;
        }

        FlowNodeEntity nodeEntity;
        if (element instanceof SubProcess) {
            nodeEntity = new FlowNodeEntity();
            nodeEntity.setFlowDefinitionId(this.processDefinitionId);
            nodeEntity.setId(IdWorker.getIdStr());
            if (StringUtils.isNotBlank(element.getName())) {
                nodeEntity.setName(element.getName());
            } else {
                nodeEntity.setName("子流程");
            }
            nodeEntity.setType(FlowNodeEntity.Type.SUBPROCESS);
            nodeEntity.setDefinitionId(defId);
            nodeEntity.setParentId("0");
            nodeService.save(nodeEntity);

            //遍历子流程内的节点
            for (FlowElement flowElement : ((SubProcess) element).getFlowElements()) {
                saveNode(flowElement, nodeEntity.getId());
            }
        }

        nodeEntity = new FlowNodeEntity();
        nodeEntity.setId(IdWorker.getIdStr());
        nodeEntity.setName(element.getName());
        nodeEntity.setDefinitionId(defId);
        nodeEntity.setInitiatorView(1);
        nodeEntity.setAssigneeView(1);
        nodeEntity.setTaskCode(element.getId());
        nodeEntity.setFlowDefinitionId(this.processDefinitionId);
        if (subProcessId != null) {
            //子流程ID不为null说明为子流程内的节点
            nodeEntity.setParentId(subProcessId);
        }

        if (element instanceof UserTask) {
            nodeEntity.setType(FlowNodeEntity.Type.USER_TASK);
            nodeEntity.setParentId("0");

            confUserTask((UserTask) element, nodeEntity.getId());
            configButton(nodeEntity.getId());
            setNodeConfig(nodeEntity.getId());
        } else if (element instanceof StartEvent) {
            if (StringUtils.isBlank(nodeEntity.getName())) {
                nodeEntity.setName("开始节点");
            }
            nodeEntity.setType(FlowNodeEntity.Type.START_EVENT);
            nodeEntity.setParentId("0");
        } else if (element instanceof EndEvent) {
            if (StringUtils.isBlank(nodeEntity.getName())) {
                nodeEntity.setName("结束节点");
            }
            nodeEntity.setType(FlowNodeEntity.Type.END_EVENT);
            nodeEntity.setParentId("0");
        } else if (element instanceof InclusiveGateway) {
            nodeEntity.setType(FlowNodeEntity.Type.INCLUSIVE_GATEWAY);
            nodeEntity.setParentId("0");
        } else if (element instanceof ParallelGateway) {
            nodeEntity.setType(FlowNodeEntity.Type.PARALLEL_GATEWAY);
            nodeEntity.setParentId("0");
        } else if (element instanceof ExclusiveGateway) {
            nodeEntity.setName("流转网关");
            nodeEntity.setType(FlowNodeEntity.Type.EXCLUSIVE_GATEWAY);
            nodeEntity.setParentId("0");
            saveSequenceFlow(((ExclusiveGateway) element).getOutgoingFlows(), nodeEntity);
        } else {
            return;
        }
        nodeService.save(nodeEntity);
    }

    /**
     * 保存流转网关条件线
     */
    private void saveSequenceFlow(List<SequenceFlow> sequenceFlows, FlowNodeEntity parentNode) {

        for (SequenceFlow sequenceFlow : sequenceFlows) {
            FlowNodeEntity sequenceFlowNode = new FlowNodeEntity();
            String sequenceFlowName = sequenceFlow.getName();
            String targetSequenceName = "无节点名称";
            if (sequenceFlow.getTargetFlowElement().getName() != null) {
                targetSequenceName = sequenceFlow.getTargetFlowElement().getName();
            }
            if (StringUtils.isNotBlank(sequenceFlowName)) {
                sequenceFlowNode.setName(sequenceFlowName + "->" + targetSequenceName);
            } else {
                sequenceFlowNode.setName("条件流转线->" + sequenceFlow.getTargetFlowElement().getName());
            }
            sequenceFlowNode.setType(FlowNodeEntity.Type.SEQUENCE_FLOW);
            sequenceFlowNode.setParentId(parentNode.getId());
            sequenceFlowNode.setDefinitionId(this.defId);
            sequenceFlowNode.setTaskCode(sequenceFlow.getId());
            sequenceFlowNode.setId(IdWorker.getIdStr());
            nodeService.save(sequenceFlowNode);

            FlowSequenceEntity sequenceEntity = new FlowSequenceEntity();
            sequenceEntity.setSourceId(sequenceFlow.getSourceRef());
            sequenceEntity.setTargetId(sequenceEntity.getTargetId());
            sequenceEntity.setNodeId(sequenceFlowNode.getId());
            sequenceService.save(sequenceEntity);
        }
    }

    /**
     * 配置用户任务
     */
    private void confUserTask(UserTask userTask, String nodeId) {
        //配置表单
        if (StringUtils.isNotBlank(userTask.getFormKey())) {

            FlowFormEntity formEntity = new FlowFormEntity();
            formEntity.setFormKey(userTask.getFormKey());
            formEntity.setNodeId(nodeId);
            formEntity.setType(FlowFormEntity.Type.DEFAULT);

            formService.save(formEntity);
        }

        //配置用户
        if (StringUtils.isNotBlank(userTask.getAssignee())) {

            FlowUserEntity userEntity = new FlowUserEntity();
            userEntity.setSort(0);
            userEntity.setType(FlowUserEntity.Type.普通用户);
            userEntity.setValue(userTask.getAssignee());
            userEntity.setNodeId(nodeId);

            userService.save(userEntity);
        }
        List<String> tempUsers = userTask.getCandidateUsers();
        if (null != tempUsers && tempUsers.size() > 0) {
            FlowUserEntity userEntity = new FlowUserEntity();
            userEntity.setSort(0);
            userEntity.setType(FlowUserEntity.Type.普通用户);
            userEntity.setValue(StringUtils.join(tempUsers));
            userEntity.setNodeId(nodeId);

            userService.save(userEntity);
        }

        //配置用户组
        tempUsers = userTask.getCandidateGroups();
        if (null != tempUsers && tempUsers.size() > 0) {
            FlowUserEntity userEntity = new FlowUserEntity();
            userEntity.setSort(0);
            userEntity.setType(FlowUserEntity.Type.用户组);
            userEntity.setValue(StringUtils.join(tempUsers));
            userEntity.setNodeId(nodeId);

            userService.save(userEntity);
        }


    }


    /**
     * 配置流程节点按钮
     */
    public void configButton(String nodeId) {
        Integer[] typeArray = {FlowButtonEntity.Type.同意, FlowButtonEntity.Type.分发,FlowButtonEntity.Type.驳回};

        for (int i = 0; i < typeArray.length; i++) {
            FlowButtonEntity buttonEntity = new FlowButtonEntity();
            buttonEntity.setSort(i);
            buttonEntity.setCreateTime(new Date());
            buttonEntity.setNodeId(nodeId);
            buttonEntity.setType(typeArray[i]);
            buttonService.save(buttonEntity);
        }
    }

    /**
     * 配置流程节点
     */
    public void setNodeConfig(String nodeId) {
        FlowNodeConfigEntity flowNodeConfigEntity = new FlowNodeConfigEntity();
        flowNodeConfigEntity.setCreateTime(new Date());
        flowNodeConfigEntity.setNodeId(nodeId);
        flowNodeConfigEntity.setRejectNodeType(FlowNodeConfigEntity.Type.发起人);
        flowNodeConfigService.save(flowNodeConfigEntity);
    }


    /**
     * 业务流程定义ID
     */
    private String defId;

    /**
     * 工作流流程定义ID
     */
    private String processDefinitionId;

    private FlowNodeService nodeService;

    private FlowFormService formService;

    private FlowUserService userService;

    private FlowVarService varService;

    private FlowSequenceService sequenceService;

    private FlowButtonService buttonService;

    private FlowNoticeService noticeService;

    private FlowNodeConfigService flowNodeConfigService;

}
