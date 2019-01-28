package com.redimybase.manager.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.manager.security.dto.UserListPage;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.entity.dto.UserAddressListDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
//@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {


    public List<UserAddressListDTO> getAddressList(@Param("orgId") String orgId,@Param("orgName") String orgName);

    public UserListPage<UserEntity> getUserByOrgId(UserListPage<UserEntity> page, @Param("orgId") String orgId, @Param("phone") String phone, @Param("userName") String userName);

    public String getOrgFullName(@Param("orgId") String orgId);

    public List<String> getUserIdByOrgId(@Param("orgId") String orgId, @Param("status") String status);
}
