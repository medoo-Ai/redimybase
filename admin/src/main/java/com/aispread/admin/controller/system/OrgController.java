package com.aispread.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.security.entity.OrgEntity;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.mapper.OrgMapper;
import com.redimybase.manager.security.service.impl.OrgServiceImpl;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;

/**
 * 组织部门Controller
 * Created by Vim 2018/11/23 16:52
 *
 * @author Vim
 */
@RestController
@RequestMapping("org")
@Api(tags = "组织部门接口")
public class OrgController extends TableController<String, OrgEntity, OrgMapper, OrgServiceImpl> {

    @Override
    public Object query(HttpServletRequest request) {
        TableModel<OrgEntity> model = new TableModel<>();
        Page<OrgEntity> page = (Page<OrgEntity>) buildPageRequest(request);
        if (page == null) {
            page = new Page<>(1, 8);
        }

        QueryWrapper<OrgEntity> queryWrapper = buildWrapper(getQueryColumn(request), getQueryValue(request));

        if (null == queryWrapper) {
            queryWrapper = new QueryWrapper<>();
        }
        queryWrapper.and(i -> i.eq("status", OrgEntity.Status.启用).or().eq("status", OrgEntity.Status.禁用));
        model.setData(getService().page(page, buildPageWrapper(queryWrapper, getQueryKey(request), getQuerySearch(request))));
        return model;
    }

    @PostMapping(value = "orgNodeList")
    @RequiresPermissions(value = {"system_org"})
    @ApiOperation(value = "获取组织部门树")
    public R<?> orgNodeList(String type) {
        return new R<>(service.orgNodeList(type));
    }


    @Override
    public void beforeSave(OrgEntity entity) {
        UserEntity userEntity = SecurityUtil.getCurrentUser();
        if (null == userEntity) {
            throw new BusinessException(R.失败, "用户凭证过期,请尝试重新登录");
        }
        if (StringUtils.isBlank(entity.getName())) {
            throw new BusinessException(500, "名称已存在,请尝试重新输入");
        }
        if (StringUtils.isBlank(entity.getId())) {
            entity.setStatus(OrgEntity.Status.启用);
            //key相同不添加
            if (service.count(new QueryWrapper<OrgEntity>().in("status", OrgEntity.Status.启用, OrgEntity.Status.禁用).and(i -> i.eq("code", entity.getCode()))) > 0) {
                throw new BusinessException(R.失败, "组织code已存在");
            }
            if (StringUtils.isBlank(entity.getParentId())) {
                entity.setParentId("0");
            }
            entity.setCreateTime(new Date());
            entity.setStatus(OrgEntity.Status.启用);
            entity.setCreator(userEntity.getUserName());
            entity.setCreateId(userEntity.getId());
        } else {
            entity.setReviser(userEntity.getUserName());
            entity.setReviserId(userEntity.getId());
            entity.setUpdateTime(new Date());
        }

        if (entity.getType() == null) {
            entity.setType(OrgEntity.Type.公司);
        }
        super.beforeSave(entity);
    }

    @Override
    public R<?> delete(String id) {
        OrgEntity entity = new OrgEntity();
        entity.setId(id);
        entity.setStatus(OrgEntity.Status.删除);
        service.updateById(entity);
        return R.ok();
    }

    @Override
    public R<?> deleteBatchIds(String ids) {
        String[] idArray = StringUtils.split(ids, ",");

        for (String id : idArray) {
            this.delete(id);
        }
        return R.ok();
    }

    @Autowired
    private OrgServiceImpl service;

    @Override
    protected OrgServiceImpl getService() {
        return service;
    }
}
