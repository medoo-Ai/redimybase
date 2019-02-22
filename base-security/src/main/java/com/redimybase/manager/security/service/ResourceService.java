package com.redimybase.manager.security.service;

import com.redimybase.manager.security.dto.ResourceDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.redimybase.manager.security.entity.ResourceEntity;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
public interface ResourceService extends IService<ResourceEntity> {

    List<String> getResKeyList(String userId);

    List<String> getResNameList(String userId);

    List<ResourceDto> getMenuByUserId(String currentUserId, Integer button);

    List<ResourceDto> getMenuByRoleId(String roleId);

    /**
     * 获取所有菜单节点
     */
    List<ResourceDto> menuNodeList();

    /**
     * 获取指定用户的菜单树
     * 只能返回二级以内菜单
     */
    List<ResourceDto> menuNodeListByUserId(String userId,Integer button);

}
