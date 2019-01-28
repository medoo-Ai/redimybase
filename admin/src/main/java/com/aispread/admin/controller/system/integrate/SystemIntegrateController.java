package com.aispread.admin.controller.system.integrate;

import com.aispread.manager.dict.entity.DictEntity;
import com.aispread.manager.dict.service.DictService;
import com.aispread.manager.integrate.entity.SystemIntegrateEntity;
import com.aispread.manager.integrate.mapper.SystemIntegrateMapper;
import com.aispread.manager.integrate.service.impl.SystemIntegrateServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.web.TableController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统集成接口
 * Created by Vim 2019/1/26 13:46
 *
 * @author Vim
 */
@RestController
@RequestMapping("system/integrate")
@Api(tags = "系统集成(角色切换)接口")
public class SystemIntegrateController extends TableController<String, SystemIntegrateEntity, SystemIntegrateMapper, SystemIntegrateServiceImpl> {


    @PostMapping("systemList")
    @ApiOperation(value = "获取系统信息")
    public R<?> systemList() {
        return new R<>(StringUtils.split(dictService.getOne(new QueryWrapper<DictEntity>().eq("dict_key", "role_system_name_list").select("id,dict_value")).getDictValue(), ","));
    }

    @Autowired
    private DictService dictService;

    @Autowired
    private SystemIntegrateServiceImpl service;

    @Override
    protected SystemIntegrateServiceImpl getService() {
        return service;
    }
}
