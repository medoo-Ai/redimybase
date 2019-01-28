package com.redimybase.flowable.controller;

import com.aispread.manager.flowable.dto.*;
import com.aispread.manager.flowable.service.ActHiTaskinstService;
import com.aispread.manager.flowable.service.ActRuTaskService;
import com.redimybase.flowable.controller.dto.ProcessDefinitionDTO;
import com.redimybase.flowable.controller.dto.ProcessInstanceDTO;
import com.redimybase.flowable.service.ProcessHandleService;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.*;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程监控Controller
 * Created by Vim 2018/10/12 0012 22:19
 */
@RestController
@RequestMapping("process/monitor")
@Api(tags = "流程监控接口")
public class ProcessMonitorController {

    /**
     * 直接完成任务
     *
     * @param taskId 任务ID
     */
    @RequestMapping("complete")
    @ApiOperation(value = "直接完成任务")
    public R<?> complete(@ApiParam(value = "任务ID") String taskId) {
        taskService.complete(taskId);
        return R.ok();
    }

    /**
     * 恢复/挂起任务
     *
     * @param handle 操作标识
     * @param taskId 任务ID
     */
    @PostMapping("setTaskStatus")
    @ApiOperation(value = "恢复/挂起任务")
    public R<?> setTaskStatus(@ApiParam(value = "操作动作(stop:挂起,recovery:恢复)") String handle, @ApiParam(value = "任务ID") String taskId) {
        if ("stop".equals(handle)) {
            processHandleService.stopTask(taskId);
        } else if ("recovery".equals(handle)) {
            processHandleService.recoveryTask(taskId);

        }
        return R.ok();
    }

    /**
     * 给任务分配审批人
     */
    @PostMapping("setAssignee")
    @ApiOperation(value = "给任务分配审批人")
    public R<?> setAssignee(@ApiParam(value = "任务ID") String taskId, @ApiParam(value = "用户ID") String userId) {
        taskService.setAssignee(taskId, userId);
        return R.ok();
    }

    /**
     * 获取流程任务列表
     */
    @PostMapping("taskList")
    @ApiOperation(value = "获取流程任务列表")
    public R<?> taskList(FlowTodoTaskQuery query) {
        TaskPage<FlowTodoTask> page = new TaskPage<>();
        page.setCurrent(query.getPage());
        page.setSize(query.getPageSize());
        if (StringUtils.isNotBlank(query.getOrderBy())) {
            if (query.isAsc()) {
                page.setAsc(query.getOrderBy());
            } else {
                page.setDesc(query.getOrderBy());
            }
        }
        return new R<>(actRuTaskService.taskList(page, query));
    }


    /**
     * 获取已办任务列表
     */
    @PostMapping("doneTaskList")
    @ApiOperation(value = "获取已办任务列表")
    public R<?> taskList(FlowDoneTaskQuery query) {
        TaskPage<FlowDoneTask> page = new TaskPage<>();
        page.setCurrent(query.getPage());
        page.setSize(query.getPageSize());
        if (StringUtils.isNotBlank(query.getOrderBy())) {
            if (query.isAsc()) {
                page.setAsc(query.getOrderBy());
            } else {
                page.setDesc(query.getOrderBy());
            }
        }
        return new R<>(actHiTaskinstService.taskList(page, query));
    }

    /**
     * 获取已申请任务列表
     */
    @PostMapping("applyTaskList")
    @ApiOperation(value = "获取已申请任务列表")
    public R<?> applyTaskList(FlowApplyTaskQuery query) {
        TaskPage<FlowApplyTask> page = new TaskPage<>();
        page.setCurrent(query.getPage());
        page.setSize(query.getPageSize());
        if (StringUtils.isNotBlank(query.getOrderBy())) {
            if (query.isAsc()) {
                page.setAsc(query.getOrderBy());
            } else {
                page.setDesc(query.getOrderBy());
            }
        }
        return new R<>(actHiTaskinstService.applyTaskList(page, query));
    }


    /**
     * 获取所有流程实例
     */
    @RequestMapping("queryInstance")
    @ApiOperation(value = "获取所有流程实例")
    public R<?> queryInstance(@ApiParam("当前页码") Integer current, @ApiParam("当前页数据大小") Integer size) {
        if (current == null) {
            throw new BusinessException(500, "缺少参数[current]");
        }
        if (size == null) {
            throw new BusinessException(500, "缺少参数[size]");
        }
        return new R<>(instanceToDto(runtimeService.createProcessInstanceQuery().listPage(current, size)));
    }

    private List<ProcessInstanceDTO> instanceToDto(List<ProcessInstance> processInstances) {
        List<ProcessInstanceDTO> processInstanceDTOS = new ArrayList<>();

        processInstances.forEach(p -> {
            ProcessInstanceDTO dto = new ProcessInstanceDTO();
            dto.setActivityId(p.getActivityId());
            dto.setBusinessKey(p.getBusinessKey());
            dto.setDeploymentId(p.getDeploymentId());
            dto.setDescription(p.getDescription());
            dto.setEnded(p.isEnded());
            dto.setName(p.getName());
            dto.setParentId(p.getParentId());
            dto.setProcessDefinitionId(p.getProcessDefinitionId());
            dto.setProcessDefinitionKey(p.getProcessDefinitionKey());
            dto.setProcessDefinitionName(p.getProcessDefinitionName());
            dto.setProcessDefinitionVersion(p.getProcessDefinitionVersion());
            dto.setProcessInstanceId(p.getProcessInstanceId());
            dto.setStartTime(p.getStartTime());
            dto.setStartUserId(p.getStartUserId());
            processInstanceDTOS.add(dto);
        });
        return processInstanceDTOS;
    }

    /**
     * 获取所有流程定义
     */
    @RequestMapping("queryDef")
    @ApiOperation(value = "获取所有流程定义")
    public R<?> queryDef(@ApiParam("当前页码") Integer current, @ApiParam("当前页数据大小") Integer size) {
        if (current == null) {
            throw new BusinessException(500, "缺少参数[current]");
        }
        if (size == null) {
            throw new BusinessException(500, "缺少参数[size]");
        }
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().listPage(current, size);

        return new R<>(defToDto(processDefinitions));
    }

    private List<ProcessDefinitionDTO> defToDto(List<ProcessDefinition> processDefinitions) {
        List<ProcessDefinitionDTO> processDefinitionDTOS = new ArrayList<>();
        processDefinitions.forEach(p -> {
            ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
            dto.setCategory(p.getCategory());
            dto.setDeploymentId(p.getDeploymentId());
            dto.setDescription(p.getDescription());
            dto.setKey(p.getKey());
            dto.setName(p.getName());
            dto.setResourceName(p.getResourceName());
            dto.setVersion(p.getVersion());
            processDefinitionDTOS.add(dto);
        });
        return processDefinitionDTOS;
    }


    @Autowired
    private ActHiTaskinstService actHiTaskinstService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ActRuTaskService actRuTaskService;

    @Autowired
    private ProcessHandleService processHandleService;


}
