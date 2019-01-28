package com.aispread.manager.form.service.impl;

import com.aispread.manager.flowable.entity.FlowFormEntity;
import com.aispread.manager.flowable.service.FlowFormService;
import com.aispread.manager.form.dto.FormField;
import com.aispread.manager.form.entity.FormEntity;
import com.aispread.manager.form.entity.FormFieldEntity;
import com.aispread.manager.form.mapper.FormMapper;
import com.aispread.manager.form.service.FormFieldService;
import com.aispread.manager.form.service.FormService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.framework.bean.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-01-02
 */
@Service
public class FormServiceImpl extends ServiceImpl<FormMapper, FormEntity> implements FormService {

    @Override
    public R<?> saveForm(FormEntity entity) {
        String json = entity.getJson();
        List<FormField> formFields = JSONArray.parseArray(json, FormField.class);
        entity.setFieldNum(formFields.size());

        if (StringUtils.isNotEmpty(entity.getFormKey())) {
            FormEntity formKey = getOne(new QueryWrapper<FormEntity>().eq("id", entity.getId()).select("form_key"));
            if (formKey != null) {
                //同步工作流配置的formKey
                FlowFormEntity flowFormEntity = new FlowFormEntity();
                flowFormEntity.setFormKey(entity.getFormKey());
                flowFormService.update(flowFormEntity, new QueryWrapper<FlowFormEntity>().eq("form_key", formKey.getFormKey()));
            }
        }

        save(entity);


        for (FormField formField : formFields) {
            FormFieldEntity formFieldEntity = new FormFieldEntity();
            formFieldEntity.setFormId(entity.getId());
            formFieldEntity.setType(formField.getType());
            formFieldEntity.setAdding(formField.isAdding());
            formFieldEntity.setFieldName(formField.getFieldName());
            formFieldEntity.setLabel(formField.getLabel());
            formFieldEntity.setName(formField.getName());
            formFieldEntity.setOptions(JSON.toJSONString(formField.getOptions()));
            formFieldEntity.setRequired(formField.isRequired());
            formFieldEntity.setRequiredMessage(formField.getRequiredMessage());
            formFieldEntity.setSpan(formField.getSpan());
            formFieldService.save(formFieldEntity);
        }
        return R.ok();
    }

    @Override
    public FormEntity getFormByNodeId(String nodeId) {
        return baseMapper.getFormByNodeId(nodeId);
    }


    @Autowired
    private FlowFormService flowFormService;

    @Autowired
    private FormFieldService formFieldService;
}
