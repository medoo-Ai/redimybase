package com.redimybase.manager.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.entity.UserRoleEntity;
import com.redimybase.manager.security.mapper.UserRoleMapper;
import com.redimybase.manager.security.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    @Override
    public void setUserRole(String id, String roleIds, UserEntity currentUser) {
        remove(new QueryWrapper<UserRoleEntity>().eq("user_id", id));

        String[] roleIdList = StringUtils.split(roleIds, ",");

        for (String roleId : roleIdList) {
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setCreateTime(new Date());
            userRoleEntity.setCreator(currentUser.getUserName());
            userRoleEntity.setCreatorId(currentUser.getId());
            userRoleEntity.setRoleId(roleId);
            userRoleEntity.setUserId(id);
            save(userRoleEntity);
        }
    }
}
