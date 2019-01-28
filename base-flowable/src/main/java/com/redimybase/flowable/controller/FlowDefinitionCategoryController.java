package com.redimybase.flowable.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.web.TableController;
import com.aispread.manager.flowable.entity.FlowDefinitionCategoryEntity;
import com.aispread.manager.flowable.mapper.FlowDefinitionCategoryMapper;
import com.aispread.manager.flowable.service.impl.FlowDefinitionCategoryServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 流程模板种类Controller
 * Created by Vim 2019/1/5 11:45
 *
 * @author Vim
 */
@RestController
@RequestMapping("flow/category")
@Api(tags = "流程模板种类接口")
public class FlowDefinitionCategoryController extends TableController<String, FlowDefinitionCategoryEntity, FlowDefinitionCategoryMapper, FlowDefinitionCategoryServiceImpl> {

    /**
     * 获取流程种类树
     */
    @PostMapping("getCategoryTree")
    public R<?> getCategoryTree(String name) {
        return service.getCategoryTree(name);
    }


    @Autowired
    private FlowDefinitionCategoryServiceImpl service;

    @Override
    protected FlowDefinitionCategoryServiceImpl getService() {
        return service;
    }

}
