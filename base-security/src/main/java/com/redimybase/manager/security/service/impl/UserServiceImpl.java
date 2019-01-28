package com.redimybase.manager.security.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.manager.security.dto.UserListPage;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.entity.dto.UserAddressListDTO;
import com.redimybase.manager.security.mapper.UserMapper;
import com.redimybase.manager.security.service.UserService;
import com.redimybase.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {


    /**
     * 重置密码
     */
    @Override
    public String resetPwd(String id) {
        try {
            String newPwd = "123456";//SecurityUtil.getResetPwd(8);
            String saltPwd = SecurityUtil.encryptPwd(newPwd, pwdSalt);
            UserEntity userEntity = new UserEntity();
            userEntity.setId(id);
            userEntity.setPassword(saltPwd);
            updateById(userEntity);
            return newPwd;
        } catch (Exception e) {
            log.error("用户重置密码出错,msg:{}", e);
        }
        return null;
    }

    @Override
    public Map<String, List<UserAddressListDTO>> getAddressList(String orgId,String orgName) {
        if (StringUtils.isEmpty(orgId)) {
            orgId = null;
        }
        if (StringUtils.isEmpty(orgName)) {
            orgName = null;
        }else{
            orgName = "%" + orgName + "%";
        }
        List<UserAddressListDTO> addressList = baseMapper.getAddressList(orgId,orgName);
        addressList.forEach(userAddressListDTO -> userAddressListDTO.setOrgName(baseMapper.getOrgFullName(userAddressListDTO.getOrgId())));
        return new UserAddressSort().sort(addressList);
    }

    @Override
    public String getOrgFullName(String orgId) {
        return baseMapper.getOrgFullName(orgId);
    }


    @Override
    public void updatePassword(String oldPwd, String newPwd, UserEntity currentUser) {

        try {
            String saltOldPwd = SecurityUtil.encryptPwd(oldPwd, pwdSalt);
            if (StringUtils.equals(currentUser.getPassword(), saltOldPwd)) {
                String saltNewPwd = SecurityUtil.encryptPwd(newPwd, pwdSalt);
                currentUser.setPassword(saltNewPwd);
                updateById(currentUser);
            }
        } catch (Exception e) {
            log.error("用户修改密码出错,msg:{}", e);
        }
    }

    @Override
    public UserListPage<UserEntity> getUserByOrdId(UserListPage<UserEntity> page) {
        if (null == page.getPhone()) {
            page.setPhone("");
        }
        if (null == page.getUserName()) {
            page.setUserName("");
        }
        return baseMapper.getUserByOrgId(page, page.getOrgId(), "%" + page.getPhone() + "%", "%" + page.getUserName() + "%");
    }

    @Override
    public List<String> getUserIdByOrgId(String orgId, String status) {
        return baseMapper.getUserIdByOrgId(orgId, status);
    }


    @Value("${redi.pwd.salt}")
    public String pwdSalt;

}
