package com.aispread.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.security.dto.UserListPage;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.entity.UserOrgEntity;
import com.redimybase.manager.security.mapper.UserMapper;
import com.redimybase.manager.security.service.UserOrgService;
import com.redimybase.manager.security.service.UserRoleService;
import com.redimybase.manager.security.service.impl.UserServiceImpl;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;

/**
 * 用户信息Controller
 * Created by Vim 2018/11/23 13:30
 *
 * @author Vim
 */
@RestController
@RequestMapping("user")
@Api(tags = {"用户信息接口"})
public class UserController extends TableController<String, UserEntity, UserMapper, UserServiceImpl> {


    /**
     * 获取用户列表
     */
    @PostMapping("getUserList")
    @ApiOperation(value = "获取用户列表")
    public R<?> getUserList( UserListPage<UserEntity> page) {
        return new R<>(service.getUserByOrdId(page));
    }

    /**
     * 获取人员通讯录
     *
     * @param orgId 组织ID,不传默认查询全部人员通讯录
     */
    @PostMapping("getAddressList")
    @RequiresPermissions("system_address_list")
    @ApiOperation(value = "获取人员通讯录")
    public R<?> getAddressList(@ApiParam(value = "组织ID,不传默认查询全部人员通讯录") String orgId,@ApiParam(value = "组织名称") String orgName) {
        return new R<>(service.getAddressList(orgId, orgName));
    }

    /**
     * 重置随机密码
     */
    @PostMapping("resetPwd")
    @RequiresPermissions("system_user")
    @ApiOperation(value = "重置随机密码")
    public R<?> resetPassword(@ApiParam(value = "用户ID") String id) {
        return R.ok("新密码为:" + service.resetPwd(id));
    }

    /**
     * 用户修改密码
     */
    @PostMapping("updatePassword")
    @ApiOperation(value = "用户修改密码")
    public R<?> updatePassword(@ApiParam(value = "旧密码") String oldPwd,@ApiParam(value = "新密码") String newPwd) {
        UserEntity currentUser = SecurityUtil.getCurrentUser();
        if (null == currentUser) {
            return R.fail("用户凭证过期,请尝试重新登录");
        }
        service.updatePassword(oldPwd, newPwd, currentUser);
        return R.ok("修改成功,请牢记新密码");
    }

    /**
     * 设置用户角色
     */
    @PostMapping("serUserRole")
    @RequiresPermissions("system_user")
    @ApiOperation(value = "设置用户角色")
    public R<?> serUserRole(@ApiParam(value = "用户ID") String id,@ApiParam(value = "角色ID集合") String roleIdList) {
        UserEntity currentUser = SecurityUtil.getCurrentUser();

        if (null == currentUser) {
            throw new BusinessException(R.失败, "用户凭证过期,请尝试重新登录");
        }
        userRoleService.setUserRole(id, roleIdList, currentUser);
        return R.ok();
    }

    /**
     * 迁移用户(从一个组织迁移到另一个组织)
     */
    @PostMapping("move")
    @ApiOperation(value = "迁移用户(从一个组织迁移到另一个组织)")
    public R<?> move(@ApiParam(value = "需要迁移的组织ID") String sourceOrgId, @ApiParam(value = "目标组织ID") String targetOrgId) {
        return R.ok();
    }

    @Override
    public void beforeSave(UserEntity entity) {
        UserEntity currentUser = SecurityUtil.getCurrentUser();
        if (null == currentUser) {
            throw new BusinessException(R.失败, "用户凭证过期");
        }
        if (StringUtils.isBlank(entity.getId())) {

            if (service.count(new QueryWrapper<UserEntity>().in("status", UserEntity.Status.ENABLE, UserEntity.Status.DISABLE)
                    .and(i -> i.eq("account", entity.getAccount()).or().eq("phone", entity.getPhone()).or()
                            .eq("email", entity.getEmail()).or().eq("id_no", entity.getIdNo()))) > 0) {

                throw new BusinessException(R.失败, "登录账号/手机号/邮箱/身份证已存在,请尝试重新添加");
            }


            //如果密码为空默认123456
            if (StringUtils.isBlank(entity.getPassword())) {
                try {
                    entity.setPassword(SecurityUtil.encryptPwd(defaultPassword,pwdSalt));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            entity.setStatus(UserEntity.Status.ENABLE);
            entity.setCreateTime(new Date());
            entity.setCreator(currentUser.getUserName());
            entity.setCreatorId(currentUser.getId());

        } else {
            entity.setUpdateTime(new Date());
            entity.setReviserId(currentUser.getId());
            entity.setReviser(currentUser.getUserName());
        }
        super.beforeSave(entity);
    }

    @Override
    public void afterSave(UserEntity entity) {
        userOrgService.remove(new QueryWrapper<UserOrgEntity>().eq("user_id", entity.getId()));

        UserOrgEntity userOrgEntity = new UserOrgEntity();
        userOrgEntity.setOrgId(entity.getOrgId());
        userOrgEntity.setUserId(entity.getId());
        userOrgEntity.setCreateTime(new Date());
        userOrgService.save(userOrgEntity);
    }

    @Override
    public R<?> delete(String id) {
        if (StringUtils.isBlank(id)) {
            return R.fail();
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setStatus(UserEntity.Status.DELETED);
        service.updateById(userEntity);
        return R.ok();
    }

    @Override
    public R<?> deleteBatchIds(String ids) {
        for (String id : StringUtils.split(ids, ",")) {
            this.delete(id);
        }
        return R.ok();
    }
    @Value("${redi.pwd.salt}")
    public String pwdSalt;

    @Value("${redi.default.init.password}")
    private String defaultPassword;

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserOrgService userOrgService;

    @Override
    protected UserServiceImpl getService() {
        return service;
    }

}