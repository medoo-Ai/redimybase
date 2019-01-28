package com.aispread.admin.controller.form;

import com.aispread.manager.form.entity.FormEntity;
import com.aispread.manager.form.entity.FormFieldEntity;
import com.aispread.manager.form.mapper.FormFieldMapper;
import com.aispread.manager.form.service.FormService;
import com.aispread.manager.form.service.impl.FormFieldServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.web.TableController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 表单字段Controller
 * Created by Vim 2019/1/8 13:04
 *
 * @author Vim
 */
@RestController
@RequestMapping("form/field")
@Api(tags = "表单字段接口")
public class FormFieldController extends TableController<String, FormFieldEntity, FormFieldMapper, FormFieldServiceImpl> {


    @PostMapping("queryByFormKey")
    @ApiOperation(value = "根据表单KEY获取字段")
    public R<?> queryByFormKey(String formKey) {
        FormEntity formId = formService.getOne(new QueryWrapper<FormEntity>().eq("form_key", formKey).select("id"));

        return new R<>(service.list(new QueryWrapper<FormFieldEntity>().eq("form_id", formId.getId())));
    }

    @Autowired
    private FormService formService;

    @Autowired
    private FormFieldServiceImpl service;
    @Override
    protected FormFieldServiceImpl getService() {
        return service;
    }
}
