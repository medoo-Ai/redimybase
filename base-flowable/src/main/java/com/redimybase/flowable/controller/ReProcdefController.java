package com.redimybase.flowable.controller;

import com.redimybase.framework.bean.R;
import com.redimybase.framework.web.TableController;
import com.aispread.manager.flowable.entity.ActReProcdefEntity;
import com.aispread.manager.flowable.mapper.ReProcdefMapper;
import com.aispread.manager.flowable.service.impl.ReProcdefServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.RepositoryService;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 工作流流程定义接口
 * Created by Vim 2019/1/5 14:31
 *
 * @author Vim
 */
@RestController
@RequestMapping("flowable/procedef")
@Slf4j
@Api(tags = "工作流流程定义接口")
public class ReProcdefController extends TableController<String, ActReProcdefEntity, ReProcdefMapper, ReProcdefServiceImpl> {

    /**
     * 挂起/激活流程定义
     */
    @RequestMapping(value = "update/{state}", method = RequestMethod.POST)
    @ApiOperation(value = " 挂起/激活流程定义")
    public R<?> updateState(@ApiParam(value = "状态(active:激活,suspend:挂起)") @PathVariable("state") String state, @ApiParam(value = "流程实例ID") String instanceId,
                            RedirectAttributes redirectAttributes) {
        try {
            if (state.equals("active")) {
                log.info("message", "已激活ID为[" + instanceId + "]的流程实例。");
                repositoryService.activateProcessDefinitionById(instanceId);
            } else if (state.equals("suspend")) {
                repositoryService.suspendProcessDefinitionById(instanceId);
                log.info("message", "已挂起ID为[" + instanceId + "]的流程实例。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail();
        }
        return R.ok();
    }

    /**
     * 显示对应流程定义的流程图
     */
    @RequestMapping("viewFlowPic")
    @ApiOperation(value = "显示对应流程定义的流程图")
    public void viewFlowPic(HttpServletResponse response, @ApiParam(value = "工作流流程定义ID") String processDefId) {
        if (StringUtils.isBlank(processDefId) || StringUtils.equals("undefined", processDefId)) {
            return;
        }
        //根据流程定义ID获取到对应的流程model
        BpmnModel model = repositoryService.getBpmnModel(processDefId);
        if (null == model) {
            return;
        }
        DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();

        try {
            IOUtils.copy(generator.generateDiagram(model, "png", "宋体", "宋体", "宋体", generator.getClass().getClassLoader()), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("查看流程图出错,{}", e);
        }
    }


    @Autowired
    private ReProcdefServiceImpl service;

    @Autowired
    private RepositoryService repositoryService;


    @Override
    protected ReProcdefServiceImpl getService() {
        return service;
    }
}
