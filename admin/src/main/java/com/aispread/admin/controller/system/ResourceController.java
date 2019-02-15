package com.aispread.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.common.util.SequenceUtils;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.security.entity.OrgEntity;
import com.redimybase.manager.security.entity.ResourceEntity;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.mapper.ResourceMapper;
import com.redimybase.manager.security.service.impl.ResourceServiceImpl;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;

/**
 * 资源管理
 * Created by Irany 2018/6/15 23:21
 * @author irany
 */
@RestController
@RequestMapping(value = "resource")
@Api(tags = {"资源管理接口"})
public class ResourceController extends TableController<String, ResourceEntity, ResourceMapper, ResourceServiceImpl> {



    @PostMapping(value = "menuNodeList")
    @RequiresPermissions(value = {"system_resources"})
    @ApiOperation(value = "获取所有菜单节点")
    public R<?> menuNodeList() {
        return new R<>(service.getMenuByUserId(null,1));
    }

    @Override
    public void beforeSave(ResourceEntity entity) {
        UserEntity userEntity = SecurityUtil.getCurrentUser();
        if (null == userEntity) {
            throw new BusinessException(R.失败, "用户凭证过期");
        }
        if (StringUtils.isBlank(entity.getId())) {
            //key相同不添加
            entity.setResourceKey(SequenceUtils.getSequence());
            entity.setCreateTime(new Date());

            entity.setCreator(userEntity.getUserName());
            entity.setCreatorId(userEntity.getId());
        } else {
            if (StringUtils.isBlank(entity.getResourceKey())) {
                throw new BusinessException(R.失败, "资源KEY不能为空");
            }
            entity.setReviser(userEntity.getUserName());
            entity.setReviserId(userEntity.getId());
            entity.setUpdateTime(new Date());
        }

        //判断父级是否为根
        if (StringUtils.isBlank(entity.getParentId())) {
            entity.setParentId("0");
        }

        super.beforeSave(entity);
    }

    @Override
    public R<?> save(ResourceEntity entity) {
        super.save(entity);
        return R.ok("菜单配置后不要忘了对角色分配访问权限哦");
    }
    @Autowired
    private ResourceServiceImpl service;

    @Override
    protected ResourceServiceImpl getService() {
        return service;
    }
}
