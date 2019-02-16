package com.redimybase.security.shiro.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.service.ResourceService;
import com.redimybase.manager.security.service.RoleService;
import com.redimybase.manager.security.service.impl.UserServiceImpl;
import com.redimybase.security.shiro.constant.SecurityConst;
import com.redimybase.security.shiro.token.UserToken;
import com.redimybase.security.shiro.constant.UserStatus;
import com.redimybase.security.shiro.dao.UserCheckDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Irany 2018/5/13 12:18
 */
@Component("userCheckDaoImpl")
public class UserCheckDaoImpl implements UserCheckDao {
    /**
     * 根据用户ID获取用户token信息
     *
     * @param userId
     * @return
     */
    @Override
    public UserToken getByUserId(String userId) {
        UserEntity user = service.getById(userId);

        if (user != null) {
            if (!StringUtils.equals(user.getStatus(), UserStatus.删除)) {
                UserToken token = new UserToken();
                token.setEnabled(StringUtils.equals(user.getStatus(), UserStatus.启用));
                token.setAccount(user.getAccount());
                token.setPassword(user.getPassword());
                token.setUserId(user.getId());
                return token;
            }
        }
        return null;
    }

    /**
     * 获取用户拥有的角色
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> getRoleNameList(String userId) {
        return roleService.getRoleNameList(userId);
    }

    /**
     * 获取用户拥有的资源
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> getResNameList(String userId) {
        return resourceService.getResNameList(userId);
    }

    /**
     * 获取用户拥有的资源KEY
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> getResKeyList(String userId) {
        return resourceService.getResKeyList(userId);
    }

    /**
     * 根据账号和密码获取用户Token
     *
     * @param username
     * @param pwd
     * @return
     */
    @Override
    public UserToken getUserToken(String username, String pwd) {
        UserEntity user = null;
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();

        if (StringUtils.equals(useAccount, "1")) {
            queryWrapper.or().eq("ACCOUNT", username);
        }

        if (StringUtils.equals(useEmail, "1")) {
            queryWrapper.or().eq("EMAIL", username);
        }

        if (StringUtils.equals(usePhone, "1")) {
            queryWrapper.or().eq("PHONE", username);
        }

        if (StringUtils.equals(idNo, "1")) {
            queryWrapper.or().eq("ID_NO", username);
        }

        user = service.getOne(queryWrapper);

        if (user != null && StringUtils.equals(user.getPassword(), pwd)) {
            if (!StringUtils.equals(user.getStatus(), UserStatus.删除)) {
                return new UserToken(user.getId(), user.getAccount(), user.getPassword(), StringUtils.equals(user.getStatus(), UserStatus.启用));
            }
        }
        return null;
        //throw new IncorrectCredentialsException(SecurityConst.用户登录校验失败);
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public UserEntity getLoginInfo(String userId) {
        return service.getById(userId);
    }

    @Value("${redi.idNo}")
    public String idNo;
    @Value("${redi.usePhone}")
    public String usePhone;

    @Value("${redi.useEmail}")
    public String useEmail;

    @Value("${redi.useAccount}")
    public String useAccount;

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private RoleService roleService;


}
