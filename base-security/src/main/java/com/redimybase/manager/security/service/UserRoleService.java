package com.redimybase.manager.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.entity.UserRoleEntity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
public interface UserRoleService extends IService<UserRoleEntity> {

    /**
     * 设置用户角色
     */
    public void setUserRole(String id, String roleIds, UserEntity currentUser);
}
