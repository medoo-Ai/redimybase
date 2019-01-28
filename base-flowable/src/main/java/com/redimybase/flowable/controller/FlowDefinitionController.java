package com.redimybase.flowable.controller;

import com.aispread.manager.dict.entity.DictEntity;
import com.aispread.manager.dict.service.DictService;
import com.aispread.manager.flowable.dto.FlowDefinitionListPage;
import com.aispread.manager.flowable.entity.*;
import com.aispread.manager.flowable.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.redimybase.common.util.SequenceUtils;
import com.redimybase.flowable.cmd.SyncFlowCmd;
import com.redimybase.flowable.service.ProcessHandleService;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import com.aispread.manager.flowable.mapper.FlowDefinitionMapper;
import com.aispread.manager.flowable.service.impl.FlowDefinitionServiceImpl;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.flowable.app.model.editor.ModelRepresentation;
import org.flowable.app.rest.editor.ModelResource;
import org.flowable.app.rest.editor.ModelsResource;
import org.flowable.app.security.SecurityUtils;
import org.flowable.engine.ManagementService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 流程模板Controller
 * Created by Vim 2018/10/24 17:52
 */
@RestController
@RequestMapping("flow/definition")
@Api(value = "流程模板Controller", tags = {"流程模板接口"})
public class FlowDefinitionController extends TableController<String, FlowDefinitionEntity, FlowDefinitionMapper, FlowDefinitionServiceImpl> {


    @ApiOperation(value = "获取流程模板列表")
    @PostMapping("list")
    public R<?> list(FlowDefinitionListPage page) {
        return new R<>(service.list(page));
    }

    @ApiOperation(value = "授权", notes = "授权给哪些部门用,部门ID可以传多个以逗号隔开")
    public R<?> grant(@ApiParam("流程模板ID") String id, @ApiParam("组织ID") String orgId) {
        flowDefinitionOrgService.remove(new QueryWrapper<FlowDefinitionOrgEntity>().in("org_id", orgId));

        String[] orgIdArray = StringUtils.split(orgId, ",");

        for (String orgIdStr : orgIdArray) {
            FlowDefinitionOrgEntity flowDefinitionOrgEntity = new FlowDefinitionOrgEntity();
            flowDefinitionOrgEntity.setDefinitionId(id);
            flowDefinitionOrgEntity.setOrgId(orgIdStr);
            flowDefinitionOrgService.save(flowDefinitionOrgEntity);
        }
        return R.ok();
    }

    @Override
    @ApiOperation(value = "流程模板保存")
    public R<?> save(FlowDefinitionEntity entity) {


        if (StringUtils.isBlank(entity.getId())) {
            if (service.count(new QueryWrapper<FlowDefinitionEntity>().eq("definition_key", entity.getDefinitionKey()).or().eq("flow_definition_id", entity.getFlowDefinitionId())) > 0) {
                return R.fail("流程定义KEY或流程已关联,请尝试重新选择输入");
            }
            if (entity.getStatus() == null) {
                entity.setStatus(FlowDefinitionEntity.Status.未发布);
            }

            entity.setDefinitionKey(SequenceUtils.getSequenceInStr("DEF"));

            if (SecurityUtils.getCurrentUserObject() == null) {
                //为流程模型提供创建者
                User user = new UserEntityImpl();
                user.setId(SecurityUtil.getCurrentUser().getAccount());
                SecurityUtils.assumeUser(user);
            }

            //一键部署流程
            ModelRepresentation representation = deployment(entity);

            //取得部署好的工作流流程定义
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(entity.getDefinitionKey()).latestVersion().singleResult();
            if (processDefinition != null) {
                entity.setFlowDefinitionId(processDefinition.getId());
                entity.setFlowDefinitionKey(processDefinition.getKey());
                entity.setFlowDefinitionVersion(String.valueOf(processDefinition.getVersion()));
            }
            if (StringUtils.isBlank(entity.getIco())) {
                DictEntity dictEntity = dictService.getOne(new QueryWrapper<DictEntity>().eq("dict_key", "definition_default_ico").select("dict_value"));
                entity.setIco(dictEntity.getDictValue());
            }

            if (entity.getCompleteFirstTask() == null) {
                entity.setCompleteFirstTask(false);
            }
            entity.setId(IdWorker.getIdStr());
            entity.setModelId(representation.getId());
            entity.setCreateTime(new Date());
            entity.setCreator(Objects.requireNonNull(SecurityUtil.getCurrentUser()).getUserName());
            service.save(entity);
        } else {
            //校验流程配置
            validateDefinition(entity);
            entity.setUpdateTime(new Date());
            //TODO 加了是否同步流程定义配置字段,需要改
            FlowDefinitionEntity flowDefinitionId = service.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("id", entity.getId()).select("flow_definition_id"));
            if (entity.getFlowDefinitionId() != null) {
                if (flowDefinitionId == null || !StringUtils.equals(entity.getFlowDefinitionId(), flowDefinitionId.getFlowDefinitionId())) {
                    //如果关联了工作流定义KEY
                    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(entity.getFlowDefinitionKey()).latestVersion().singleResult();

                    //同步流程定义信息
                    managementService.executeCommand(new SyncFlowCmd(processDefinition.getId(), entity.getId()));

                    //修改了流程后保存为未发布
                    entity.setStatus(FlowDefinitionEntity.Status.未发布);
                }
            }

            service.updateById(entity);
        }

        return R.ok();
    }

    /**
     * 一键部署
     */
    private ModelRepresentation deployment(FlowDefinitionEntity entity) {
        //创建流程模型
        ModelRepresentation representation = new ModelRepresentation();
        representation.setKey(entity.getDefinitionKey());
        representation.setDescription(entity.getDescription());
        representation.setModelType(0);
        representation.setName(entity.getName());
        representation = modelsResource.createModel(representation);

        //部署流程
        actDeModelController.deployByModelId(representation.getId());

        return representation;
    }


    /**
     * 校验流程配置
     */
    private void validateDefinition(FlowDefinitionEntity entity) {
        FlowDefinitionEntity definitionStatus = service.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("id", entity.getId()).select("status"));
        if (definitionStatus == null) {
            throw new BusinessException(201, "流程模板不存在");
        }
        if (!definitionStatus.getStatus().equals(entity.getStatus())) {
            if (FlowDefinitionEntity.Status.已发布.equals(entity.getStatus())) {
                //校验流程配置是否正确
                processHandleService.validateDefinition(entity);
            }
        }
    }

    @Override
    public void afterDelete(String id) {
        FlowDefinitionEntity flowDefinitionEntity = service.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("id", id).select("id,internal"));

        if (flowDefinitionEntity == null) {
            throw new BusinessException(201, "该流程模板已不存在,请尝试刷新页面");
        }

        if (flowDefinitionEntity.isInternal()) {
            throw new BusinessException(201, "内置流程模板不能删除");
        }

        List<FlowNodeEntity> nodeEntities = flowNodeService.list(new QueryWrapper<FlowNodeEntity>().eq("definition_id", id).select("id"));

        for (FlowNodeEntity nodeEntity : nodeEntities) {
            flowNodeService.removeById(nodeEntity.getId());
            flowFormService.remove(new QueryWrapper<FlowFormEntity>().eq("node_id", nodeEntity.getId()));
            flowButtonService.remove(new QueryWrapper<FlowButtonEntity>().eq("node_id", nodeEntity.getId()));
            flowNoticeService.remove(new QueryWrapper<FlowNoticeEntity>().eq("node_id", nodeEntity.getId()));
            flowSequenceService.remove(new QueryWrapper<FlowSequenceEntity>().eq("node_id", nodeEntity.getId()));
            flowUserService.remove(new QueryWrapper<FlowUserEntity>().eq("node_id", nodeEntity.getId()));
            flowVarService.remove(new QueryWrapper<FlowVarEntity>().eq("node_id", nodeEntity.getId()));
        }
    }

    @Override
    public R<?> delete(String id) {
        FlowDefinitionEntity flowDefinitionEntity = new FlowDefinitionEntity();
        flowDefinitionEntity.setId(id);
        flowDefinitionEntity.setStatus(FlowDefinitionEntity.Status.已删除);
        service.updateById(flowDefinitionEntity);
        return R.ok();
    }

    @Override
    public R<?> deleteBatchIds(String ids) {
        String[] splitStr = StringUtils.split(ids, ",");
        for (String id : splitStr) {
            this.delete(id);
        }
        return R.ok();
    }


    /**
     * 部署流程
     */
    @PostMapping("deployModel")
    @ApiOperation(value = "部署流程")
    public R<?> deployModel(@ApiParam(value = "流程模板定义ID") String definitionId, @ApiParam("模型ID") String modelId) {
        actDeModelController.deployByModelId(modelId);
        FlowDefinitionEntity flowDefinitionEntity = service.getById(definitionId);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(flowDefinitionEntity.getFlowDefinitionKey()).latestVersion().singleResult();
        flowDefinitionEntity.setFlowDefinitionVersion(String.valueOf(processDefinition.getVersion()));
        flowDefinitionEntity.setFlowDefinitionId(processDefinition.getId());

        service.updateById(flowDefinitionEntity);
        return R.ok();
    }

    @Autowired
    private FlowDefinitionOrgService flowDefinitionOrgService;
    @Autowired
    private ActDeModelController actDeModelController;

    @Autowired
    private ModelResource modelResource;
    @Autowired
    private ModelsResource modelsResource;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private DictService dictService;

    @Autowired
    private FlowDefinitionServiceImpl service;

    @Override
    protected FlowDefinitionServiceImpl getService() {
        return service;
    }


    @Autowired
    private RepositoryService repositoryService;


    @Autowired
    private ManagementService managementService;


    @Autowired
    private FlowFormService flowFormService;

    @Autowired
    private FlowNodeService flowNodeService;

    @Autowired
    private FlowButtonService flowButtonService;


    @Autowired
    private FlowNoticeService flowNoticeService;

    @Autowired
    private FlowSequenceService flowSequenceService;

    @Autowired
    private FlowUserService flowUserService;

    @Autowired
    private FlowVarService flowVarService;

}
