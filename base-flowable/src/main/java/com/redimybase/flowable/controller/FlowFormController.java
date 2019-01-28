package com.redimybase.flowable.controller;

import com.aispread.manager.flowable.service.FlowFormContentService;
import com.aispread.manager.form.entity.FormEntity;
import com.aispread.manager.form.service.FormService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.web.TableController;
import com.aispread.manager.flowable.entity.FlowFormEntity;
import com.aispread.manager.flowable.mapper.FlowFormMapper;
import com.aispread.manager.flowable.service.impl.FlowFormServiceImpl;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 流程表单定义controller
 * Created by Vim 2018/10/25 16:05
 */
@RestController
@RequestMapping("flow/form")
@Api(tags = "流程表单配置接口")
public class FlowFormController extends TableController<String, FlowFormEntity, FlowFormMapper, FlowFormServiceImpl> {


    @Override
    public Object query(HttpServletRequest request) {
        TableModel<FlowFormEntity> model = new TableModel<>();
        Page<FlowFormEntity> page = (Page<FlowFormEntity>) buildPageRequest(request);
        if (page == null) {
            page = new Page<>(1, 8);
        }
        IPage<FlowFormEntity> pageData = getService().page(page, buildPageWrapper(buildWrapper(getQueryColumn(request), getQueryValue(request)), getQueryKey(request), getQuerySearch(request)));

        List<FlowFormEntity> records = pageData.getRecords();
        for (FlowFormEntity record : records) {
            FormEntity formName = formService.getOne(new QueryWrapper<FormEntity>().eq("form_key", record.getFormKey()).select("name"));
            record.setName(formName != null ? formName.getName() : null);
        }
        model.setData(records);
        return model;
    }

    @Override
    public R<?> save(FlowFormEntity entity) {
        service.remove(new QueryWrapper<FlowFormEntity>().eq("node_id", entity.getNodeId()));
        super.save(entity);
        /*if (service.count(new QueryWrapper<FlowFormEntity>().eq("node_id", entity.getNodeId())) > 0) {
            return R.fail("节点已存在表单,请删除该配置再继续");
        }
        //如果有多个formKey则分开新增
        if (entity.getFormKey().contains(",")) {
            String[] keys = StringUtils.split(entity.getFormKey(), ",");
            for (String key : keys) {
                entity.setId(null);
                entity.setFormKey(key);
                super.save(entity);
            }
            return R.ok();
        }*/
        return super.save(entity);
    }

    @Autowired
    private FlowFormServiceImpl service;

    @Autowired
    private FlowFormContentService flowFormContentService;


    @Autowired
    private FormService formService;

    @Override
    protected FlowFormServiceImpl getService() {
        return service;
    }
}
