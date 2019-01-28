package com.redimybase.manager.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redimybase.manager.security.entity.RoleEntity;
import com.redimybase.manager.security.entity.UserRoleEntity;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
public interface RoleService extends IService<RoleEntity> {

    List<String> getRoleNameList(String userId);

    /**
     * 根据用户ID获取角色ID集合
     */
    List<UserRoleEntity> getRoleIdByUserId(String userId);
}
