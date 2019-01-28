package com.redimybase.manager.security.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.redimybase.manager.security.dto.UserListPage;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.entity.dto.UserAddressListDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 重置密码
     */
    public String resetPwd(String id);

    /**
     * 获取人员通讯录
     */
    public Map<String, List<UserAddressListDTO>> getAddressList(String orgId,String orgName);


    /**
     * 获取组织全称
     */
    public String getOrgFullName(String orgId);


    /**
     * 修改密码
     */
    public void updatePassword(String oldPwd, String newPwd,UserEntity currentUser);

    /**
     * 根据组织ID获取用户列表
     */
    public UserListPage<UserEntity> getUserByOrdId(UserListPage<UserEntity> page);

    /**
     * 根据组织ID获取用户ID
     */
    public List<String> getUserIdByOrgId(String orgId, String status);
}
