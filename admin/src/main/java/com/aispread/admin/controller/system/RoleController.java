package com.aispread.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.security.entity.RoleEntity;
import com.redimybase.manager.security.entity.RoleResourceEntity;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.entity.UserRoleEntity;
import com.redimybase.manager.security.mapper.RoleMapper;
import com.redimybase.manager.security.service.ResourceService;
import com.redimybase.manager.security.service.RoleResourceService;
import com.redimybase.manager.security.service.impl.RoleServiceImpl;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;
import java.util.List;

/**
 * 角色管理Controller
 * Created by Vim 2018/11/23 16:56
 *
 * @author Vim
 */
@RestController
@RequestMapping("role")
@Api(tags = {"角色管理接口"})
public class RoleController extends TableController<String, RoleEntity, RoleMapper, RoleServiceImpl> {



    /**
     * 根据角色获取所有菜单资源
     */
    @PostMapping(value = "menuNodeListByRoleId")
    @RequiresPermissions(value = {"system_role"})
    @ApiOperation(value = "根据角色获取所有菜单资源")
    public R<?> menuNodeListByRoleId(@ApiParam(value = "角色ID") String id) {
        return new R<>(resourceService.getMenuByRoleId(id));
    }


    /**
     * 根据用户ID获取角色ID集合
     */
    @PostMapping("getRoleIdByUserId")
    @RequiresPermissions(value = {"system_role"})
    @ApiOperation(value = "根据用户ID获取角色ID集合")
    public R<?> getRoleIdByUserId(@ApiParam(value = "用户ID") String userId) {
        List<UserRoleEntity> roleIdByUserIdList = service.getRoleIdByUserId(userId);
        List<String> idList = new ArrayList<>();
        for (UserRoleEntity userRoleEntity : roleIdByUserIdList) {
            idList.add(userRoleEntity.getRoleId());
        }
        return new R<>(idList);
    }

    /**
     * 设置角色对应的菜单
     * @param id 角色ID
     * @param resourceIds 菜单ID列表
     * @return
     */
    @PostMapping("setRoleResource")
    @RequiresPermissions("system_role")
    @ApiOperation(value = "设置角色对应的菜单")
    public R<?> setRoleResource(@ApiParam(value = "角色ID") String id, @ApiParam(value = "菜单ID列表") String resourceIds) {
        UserEntity currentUser = SecurityUtil.getCurrentUser();
        if (null == currentUser) {
            return R.fail("用户凭证过期,请尝试重新登录");
        }
        roleResourceService.setRoleResource(id, resourceIds, currentUser);
        return R.ok();
    }

    @Override
    public void beforeSave(RoleEntity entity) {
        UserEntity userEntity = SecurityUtil.getCurrentUser();
        if (null == userEntity) {
            throw new BusinessException(R.失败, "用户凭证过期");
        }
        if (StringUtils.isBlank(entity.getId())) {
            //key相同不添加
            if (service.count(new QueryWrapper<RoleEntity>().eq("code", entity.getCode())) > 0) {
                throw new BusinessException(R.失败, "角色code已存在");
            }
            entity.setCreateTime(new Date());
            entity.setCreator(userEntity.getUserName());
            entity.setCreatorId(userEntity.getId());
        } else {
            entity.setReviser(userEntity.getUserName());
            entity.setReviserId(userEntity.getId());
            entity.setUpdateTime(new Date());
        }

        super.beforeSave(entity);
    }


    @Override
    public void afterSave(RoleEntity entity) {
        String[] resourceIdArray = StringUtils.split(entity.getReviserId(), ",");
        if (StringUtils.isNotBlank(entity.getId())) {
            for (String resourceId : resourceIdArray) {
                RoleResourceEntity roleResourceEntity = new RoleResourceEntity();
                roleResourceEntity.setResourceId(resourceId);
                roleResourceService.update(roleResourceEntity, new QueryWrapper<RoleResourceEntity>().eq("role_id", entity.getId()));
            }
        }
    }

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private RoleServiceImpl service;

    @Autowired
    private ResourceService resourceService;

    @Override
    protected RoleServiceImpl getService() {
        return service;
    }
}
