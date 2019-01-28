package com.aispread.manager.flowable.service.impl;

import com.aispread.manager.flowable.entity.FlowRuFormEntity;
import com.aispread.manager.flowable.mapper.FlowRuFormMapper;
import com.aispread.manager.flowable.service.FlowRuFormService;
import com.aispread.manager.form.dto.FormField;
import com.aispread.manager.form.entity.FormEntity;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程运行表单表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-01-11
 */
@Service
public class FlowRuFormServiceImpl extends ServiceImpl<FlowRuFormMapper, FlowRuFormEntity> implements FlowRuFormService {

    @Override
    public void saveRuForm(String businessId, String runFormId, Map<String, Object> varFields) {
        FlowRuFormEntity flowRuFormEntity = getOne(new QueryWrapper<FlowRuFormEntity>().eq("id", runFormId).select("json,id"));
        if (null == flowRuFormEntity) {
            return;
        }

        if (null != flowRuFormEntity.getJson()) {
            List<FormField> formFields = JSONArray.parseArray(flowRuFormEntity.getJson(), FormField.class);

            for (FormField formField : formFields) {
                formField.setValue(String.valueOf(varFields.get(formField.getFieldName())));
            }
            flowRuFormEntity.setJson(JSONArray.toJSONString(formFields));

            updateById(flowRuFormEntity);
        }
    }


}
