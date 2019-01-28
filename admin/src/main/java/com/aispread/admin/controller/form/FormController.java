package com.aispread.admin.controller.form;

import com.aispread.manager.form.entity.FormEntity;
import com.aispread.manager.form.entity.FormFieldEntity;
import com.aispread.manager.form.mapper.FormMapper;
import com.aispread.manager.form.service.FormFieldService;
import com.aispread.manager.form.service.impl.FormServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;

/**
 * 表单设计器Controller
 * Created by Vim 2019/1/2 15:17
 *
 * @author Vim
 */
@RestController
@RequestMapping("form")
@Api(tags = "表单设计器接口")
public class FormController extends TableController<String,FormEntity, FormMapper, FormServiceImpl> {


    @Override
    public R<?> save(FormEntity entity) {
        UserEntity currentUser = SecurityUtil.getCurrentUser();

        if (null == currentUser) {
            return R.fail("登录凭证已过期,请尝试重新登录");
        }
        if (StringUtils.isBlank(entity.getId())) {
            //新增
            entity.setCreateTime(new Date());
            entity.setCreator(currentUser.getUserName());
            FormEntity formEntity = service.getOne(new QueryWrapper<FormEntity>().eq("form_key", entity.getFormKey()).select("id"));
            if (formEntity != null) {
                service.removeById(formEntity.getId());
                formFieldService.remove(new QueryWrapper<FormFieldEntity>().eq("form_id", formEntity.getId()));
            }
        }else{
            //修改
            formFieldService.remove(new QueryWrapper<FormFieldEntity>().eq("form_id", entity.getId()));

        }
        return service.saveForm(entity);
    }

    @Override
    public void beforeDelete(String id) {
        formFieldService.remove(new QueryWrapper<FormFieldEntity>().eq("form_id", id));
    }


    @Autowired
    private FormFieldService formFieldService;

    @Autowired
    private FormServiceImpl service;



    @Override
    protected FormServiceImpl getService() {
        return service;
    }
}
