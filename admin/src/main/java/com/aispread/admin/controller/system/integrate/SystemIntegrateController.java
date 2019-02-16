package com.aispread.admin.controller.system.integrate;

import com.aispread.manager.dict.entity.DictEntity;
import com.aispread.manager.dict.service.DictService;
import com.aispread.manager.integrate.dto.SystemIntegrateInfo;
import com.aispread.manager.integrate.entity.SystemIntegrateEntity;
import com.aispread.manager.integrate.mapper.SystemIntegrateMapper;
import com.aispread.manager.integrate.service.SystemIntegrateInfoService;
import com.aispread.manager.integrate.service.impl.SystemIntegrateServiceImpl;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.web.TableController;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


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
    @ApiOperation(value = "获取选择系统下拉框信息")
    public R<?> systemList() {
        return new R<>(systemIntegrateInfoService.list(new QueryWrapper<>()));
    }


    @PostMapping("all")
    @ApiOperation("获取当前用户添加的所有系统")
    public R<?> all(){
        String currentUserId = SecurityUtil.getCurrentUserId();

        return new R<>(service.queryByUserId(currentUserId));

    }

    @PostMapping(value = "saveInfo")
    @ApiOperation("保存系统信息")
    public R<?> saveInfo(String infoList) {
        List<SystemIntegrateInfo> list = JSONArray.parseArray(infoList, SystemIntegrateInfo.class);
        for (SystemIntegrateInfo info : list) {
            SystemIntegrateEntity entity = new SystemIntegrateEntity();
            entity.setUserId(SecurityUtil.getCurrentUserId());
            entity.setSystemId(info.getSystemId());
            entity.setCreateTime(new Date());
            entity.setUserName(info.getUserName());
            entity.setId(info.getId());
            if (StringUtils.isBlank(info.getId())) {
                service.save(entity);
            }else{
                service.updateById(entity);
            }
        }
        return R.ok();
    }

    @Override
    public void beforeSave(SystemIntegrateEntity entity) {
        if (StringUtils.isBlank(entity.getId())) {
            //新增
            entity.setUserId(SecurityUtil.getCurrentUserId());
        }
    }

    @Autowired
    private DictService dictService;

    @Autowired
    private SystemIntegrateInfoService systemIntegrateInfoService;

    @Autowired
    private SystemIntegrateServiceImpl service;

    @Override
    protected SystemIntegrateServiceImpl getService() {
        return service;
    }
}
