package com.redimybase.flowable.controller;

import com.redimybase.framework.bean.R;
import com.redimybase.framework.web.TableController;
import com.aispread.manager.flowable.entity.ActDeModelEntity;
import com.aispread.manager.flowable.mapper.ActDeModelMapper;
import com.aispread.manager.flowable.service.impl.ActDeModelServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.flowable.app.domain.editor.Model;
import org.flowable.app.service.api.ModelService;
import org.flowable.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工作流流程模型Controller
 * Created by Vim 2018/10/25 14:57
 */
@RestController
@RequestMapping("flowable/model")
@Api(tags = "工作流流程模型接口")
public class ActDeModelController extends TableController<String, ActDeModelEntity, ActDeModelMapper, ActDeModelServiceImpl> {


    /**
     * 根据流程模型ID部署流程
     *
     * @param id 流程模型ID
     */
    @RequestMapping("deployByModelId")
    @ApiOperation(value = "根据流程模型ID部署流程")
    public R<?> deployByModelId(@ApiParam(value = "模型ID") String id) {
        //方法一
        /*Model model = modelService.getModel(id);
        return new R<>(
                repositoryService.createDeployment().addBytes(model.getName() + ".bpmn", modelUtils.generateBpmn20Xml(model)).deploy()
        );*/
        //方法二
        Model model = modelService.getModel(id);
        repositoryService.createDeployment().addBpmnModel(
                model.getKey() + ".bpmn",
                modelService.getBpmnModel(model)).deploy();
        return R.ok();
    }


    @Override
    public R<?> save(ActDeModelEntity entity) {
        return R.fail("接口禁用");
    }

    @Override
    public R<?> delete(String s) {
        return R.fail("接口禁用");
    }

    @Autowired
    private ModelService modelService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActDeModelServiceImpl service;

    @Override
    protected ActDeModelServiceImpl getService() {
        return service;
    }
}
