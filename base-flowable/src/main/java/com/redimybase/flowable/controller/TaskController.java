package com.redimybase.flowable.controller;

import com.aispread.manager.flowable.dto.*;
import com.aispread.manager.flowable.entity.FlowDistributeTaskEntity;
import com.aispread.manager.flowable.service.FlowOpinionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.flowable.service.ProcessHandleService;
import com.redimybase.framework.bean.R;
import com.aispread.manager.flowable.entity.ActRuTaskEntity;
import com.aispread.manager.flowable.service.ActRuTaskService;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程任务控制类
 * Created by Vim 2018/10/11 16:00
 */
@RestController
@RequestMapping("flowable/task")
@Api(tags = "流程任务接口")
public class TaskController {


    /**
     * 转办任务
     */
    @PostMapping("transfer")
    @ApiOperation(value = "转办任务")
    public R<?> transfer(@ApiParam(value = "任务ID") String taskId,@ApiParam(value = "用户ID") String userId) {
        processHandleService.transfer(taskId, userId);
        return R.ok();
    }

    /**
     * 任务沟通
     */
    @PostMapping("contact")
    @ApiOperation(value = "任务沟通")
    public R<?> contact(FlowContactDTO dto) {
        processHandleService.contact(dto);
        return R.ok();
    }

    /**
     * 发起流程视图显示
     */
    @PostMapping("startFlowView")
    @ApiOperation(value = "发起流程视图显示")
    public R<?> startFlowView(@ApiParam(value = "工作流流程定义ID") String flowDefinitionId) {
        return new R<>(processHandleService.startFlowView(flowDefinitionId));
    }

    /**
     * 待办视图显示
     */
    @PostMapping("approvalView")
    @ApiOperation(value = "待办视图显示")
    public R<?> approvalView(FlowApprovalViewQuery query) {
        return new R<>(processHandleService.approvalView(query));
    }

    /**
     * 已办任务详情视图显示
     */
    @PostMapping("doneTaskView")
    @ApiOperation(value = "已办任务详情视图显示")
    public R<?> doneTaskView(FlowDoneViewQuery query) {
        return new R<>(processHandleService.doneTaskView(query));
    }

    /**
     * 已申请任务详情视图显示
     */
    @PostMapping("applyTaskView")
    @ApiOperation(value = "已申请任务详情视图显示")
    public R<?> applyTaskView(FlowApplyViewQuery query) {
        return new R<>(processHandleService.applyTaskView(query));
    }

    /**
     * 分发任务
     */
    @PostMapping("distribute")
    @ApiOperation(value = "分发任务")
    public R<?> distribute(FlowDistributeRequest request) {
        processHandleService.distribute(request);
        return R.ok();
    }

    /**
     * 反馈任务
     */
    @PostMapping("feedback")
    @ApiOperation(value = "反馈任务")
    public R<?> feedback(FlowDistributeTaskEntity entity) {
        processHandleService.feedback(entity);
        return R.ok();
    }
    /**
     * 撤回任务
     */
    @PostMapping("recall")
    @ApiOperation(value = "撤回任务")
    public R<?> recall(@ApiParam(value = "任务ID") String taskId) {
        processHandleService.stopTask(taskId);
        return R.ok();
    }

    /**
     * 完成任务
     */
    @PostMapping("complete")
    @ApiOperation(value = "完成任务")
    public R<?> complete(FlowCompleteDTO completeDTO) {
        processHandleService.complete(completeDTO);
        return R.ok();
    }

    /**
     * 驳回任务
     */
    @PostMapping("reject")
    @ApiOperation(value = "驳回任务")
    public R<?> reject(FlowRejectDTO rejectDTO) {
        processHandleService.reject(rejectDTO);
        return R.ok();
    }
    /**
     * 获取当前用户待办任务
     *
     * @param current 当前页
     * @param size    数量
     * @return
     */
    @PostMapping("list")
    public R<?> list(Integer current, Integer size) {
        return new R<>(
                actRuTaskService.page(
                        new Page<>(current != null ? current : 1, size != null ? size : 8), new QueryWrapper<ActRuTaskEntity>()
                                .eq("ASSIGNEE_", SecurityUtil.getCurrentUserId())
                )
        );
    }


    /**
     * 查看指定节点表单
     */
    @PostMapping("viewTaskForm")
    @ApiOperation(value = "查看指定节点表单")
    public R<?> viewTaskForm(@ApiParam(value = "业务ID") String businessId,@ApiParam("任务节点CODE") String taskCode) {
        return new R<>(processHandleService.viewTaskForm(businessId, taskCode));
    }

    @Autowired
    private FormService formService;

    @Autowired
    private ActRuTaskService actRuTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private FlowOpinionService flowOpinionService;
}
