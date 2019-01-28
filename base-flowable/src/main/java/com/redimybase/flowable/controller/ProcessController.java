package com.redimybase.flowable.controller;

import com.aispread.manager.flowable.entity.FlowBusinessEntity;
import com.aispread.manager.flowable.entity.FlowDefinitionEntity;
import com.aispread.manager.flowable.service.FlowBusinessService;
import com.aispread.manager.flowable.service.FlowDefinitionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.common.util.SequenceUtils;
import com.redimybase.flowable.service.ProcessHandleService;
import com.redimybase.flowable.util.ModelUtils;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.aispread.manager.flowable.entity.ActReProcdefEntity;
import com.aispread.manager.flowable.service.impl.ReProcdefServiceImpl;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.flowable.app.service.api.ModelService;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * 流程控制类
 * Created by Vim 2018/10/11 15:09
 */
@RestController
@RequestMapping("process")
@Slf4j
@Api(tags = "流程操作接口")
public class ProcessController {


    /**
     * 获取流程定义列表
     */
    //@RequestMapping("def/list",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value = "获取流程定义列表")
    public R<?> defList(@ApiParam(value = "当前页码") Integer current, @ApiParam(value = "当前页数据大小") Integer size) {
        if (current == null) {
            throw new BusinessException(500, "缺少参数[current]");
        }
        if (size == null) {
            throw new BusinessException(500, "缺少参数[size]");
        }
        return new R<>(reProcdefService.page(new Page<>(current, size), new QueryWrapper<ActReProcdefEntity>().select("key_", "name_", "category_", "id_")));
    }

    /**
     * 流程跟踪图
     */
    @RequestMapping(value = "flowTrackPic", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "获取流程跟踪图")
    public void genProcessDiagram(HttpServletResponse httpServletResponse, @ApiParam(value = "流程实例ID") String processInstanceId) throws Exception {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        //流程走完的不显示图
        if (pi == null) {
            return;
        }
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String instanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(instanceId).list();

        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engineConfiguration = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engineConfiguration.getProcessDiagramGenerator();

        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, "宋体", "宋体", "宋体", engineConfiguration.getClassLoader(), 1.0);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int length;
        try {
            out = httpServletResponse.getOutputStream();
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
        } catch (Exception e) {
            throw new BusinessException(500, "获取流程跟踪图失败");
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 根据实例ID查看流程图
     *
     * @param processInstanceId 实例ID
     */
    @RequestMapping(value = "view/flowPng", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "根据实例ID查看流程图")
    public void viewFlowPng(HttpServletResponse response, @ApiParam(value = "流程实例ID") String processInstanceId) {

        //获取到相应实例的任务节点
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

        List<String> highLineActivity = new ArrayList<>();
        String processDefId;    //流程定义ID
        if (task == null) {
            //如果任务不存在则返回原版流程图
            processDefId = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();
        } else {
            //如果任务存在则返回跟踪流程图
            processDefId = task.getProcessDefinitionId();
            highLineActivity.add(task.getTaskDefinitionId());
        }

        //根据流程定义ID获取到对应的流程model
        BpmnModel model = repositoryService.getBpmnModel(processDefId);
        ProcessEngineConfiguration engineConfiguration = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator generator = engineConfiguration.getProcessDiagramGenerator();

        try {
            List<String> flows = new ArrayList<>();
            IOUtils.copy(generator.generateDiagram(model, "png", highLineActivity, flows, "宋体", "宋体", "宋体", engineConfiguration.getClassLoader(), 1.0), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("查看流程图出错,{}", e);
        }

    }


    /**
     * 根据流程ID启动流程
     *
     * @param id 流程ID
     */
    //@RequestMapping("startProcessById")
    @ApiOperation(value = "根据流程ID启动流程")
    public R<?> startProcessByInstanceId(@ApiParam(value = "流程实例ID") String id) {
        runtimeService.startProcessInstanceById(id);
        return R.ok();
    }

    /**
     * 根据流程定义KEY启动流程
     *
     * @param key 流程定义KEY
     */
    //@RequestMapping("startProcessByKey")
    @ApiOperation(value = "根据流程定义KEY启动流程")
    public R<?> startProcessByKey(@ApiParam(value = "工作流流程KEY") String key) {
        runtimeService.startProcessInstanceByKey(key);
        return R.ok();
    }


    /**
     * 根据流程定义KEY启动表单流程(测试用)
     *
     * @param key 流程定义KEY
     */
    //@RequestMapping("startProcessWithFormByKey")
    public R<?> startProcessWithFormByKey(String key) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).singleResult();

        Map<String, Object> variables = new HashMap<>();
        variables.put("starTime", new Date());
        variables.put("entTime", new Date());
        variables.put("reason", "回家有事");
        runtimeService.startProcessInstanceWithForm(processDefinition.getId(), "suiyi", variables, "formInstance");
        return R.ok();
    }


    /**
     * 启动流程
     */
    @RequestMapping(value = "startProcessInstance", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "启动流程")
    public R<?> startProcess(@ApiParam(value = "工作流流程定义ID", required = true) String processDefinitionId, @ApiParam(value = "启动表单字段JSON") String fieldValueJson, @ApiParam(value = "启动表单ID", required = true) String formId) {
        UserEntity currentUser = SecurityUtil.getCurrentUser();
        if (null == currentUser) {
            return R.fail("用户凭证过期,请尝试重新登录");
        }
        FlowBusinessEntity businessEntity = new FlowBusinessEntity();

        FlowDefinitionEntity definitionEntity = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("flow_definition_id", processDefinitionId).select("id,category_id"));

        businessEntity.setId(SequenceUtils.getSequence());
        businessEntity.setCategoryId(definitionEntity.getCategoryId());
        businessEntity.setCreateTime(new Date());
        businessEntity.setName("通用流程");

        flowBusinessService.save(businessEntity);
        processHandleService.startProcessInstance(currentUser, businessEntity.getId(), processDefinitionId, fieldValueJson,"通用流程");

        return R.ok();
    }


    /**
     * 根据流程定义ID部署流程
     *
     * @param processDefId 流程定义ID
     */
    @RequestMapping("deployByDefId")
    public R<?> deployByDefId(String processDefId) {
        BpmnModel model = repositoryService.getBpmnModel(processDefId);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefId).singleResult();
        repositoryService.createDeployment().addBpmnModel(processDefinition.getResourceName(), model);
        return R.ok();
    }

    /**
     * 签收任务(从组任务里面签收)
     */
    @PostMapping("claim")
    @ApiOperation(value = "签收任务(从组任务里面签收)")
    public R<?> claim(@ApiParam(value = "用户ID") String userId, @ApiParam(value = "任务ID") String taskId) {
        taskService.claim(taskId, userId);
        return R.ok();
    }

    /**
     * 终止任务
     *
     * @param taskId 任务ID
     */
    @PostMapping("stopTask")
    @ApiOperation(value = "终止任务")
    public R<?> stopTask(@ApiParam(value = "任务ID") String taskId) {
        processHandleService.stopTask(taskId);
        return R.ok();
    }

    /**
     * 恢复任务
     *
     * @param taskId 任务ID
     */
    @PostMapping("recoveryTask")
    @ApiOperation(value = "恢复任务")
    public R<?> recoveryTask(@ApiParam(value = "任务ID") String taskId) {
        processHandleService.recoveryTask(taskId);
        return R.ok();
    }


    /**
     * 任务自由跳转
     *
     * @param taskId          任务ID
     * @param targetElementId 目标节点ID
     */
    @PostMapping("jump")
    @ApiOperation(value = "任务自由跳转")
    public R<?> jumpTask(@ApiParam(value = "任务ID") String taskId, @ApiParam(value = "目标节点ID") String targetElementId) {
        processHandleService.jumpTask(taskId, targetElementId);
        return R.ok();
    }


    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ReProcdefServiceImpl reProcdefService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelUtils modelUtils;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private FlowDefinitionService flowDefinitionService;

    @Autowired
    private FlowBusinessService flowBusinessService;


}
