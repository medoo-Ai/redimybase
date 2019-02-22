package com.redimybase.manager.security.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.manager.security.entity.ResourceEntity;
import com.redimybase.manager.security.entity.UserRoleEntity;
import com.redimybase.manager.security.mapper.ResourceMapper;
import com.redimybase.manager.security.service.ResourceService;
import com.redimybase.manager.security.dto.ResourceDto;
import com.redimybase.manager.security.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, ResourceEntity> implements ResourceService {

    @Override
    public List<String> getResKeyList(String userId) {
        return baseMapper.getResKeyList(userId);
    }

    @Override
    public List<String> getResNameList(String userId) {
        return baseMapper.getResNameList(userId);
    }

    @Override
    public List<ResourceDto> getMenuByUserId(String userId, Integer button) {

        List<ResourceEntity> entities = baseMapper.getResourceByUserId(userId, button);
        //对菜单权重排序
        entities.sort(comparator);

        return getChildren("0", entities);
    }

    @Override
    public List<ResourceDto> getMenuByRoleId(String roleId) {
        List<ResourceEntity> entities = baseMapper.getResourceByRoleId(roleId);
        //对菜单权重排序
        entities.sort(comparator);

        return getChildren("0", entities);
    }


    /**
     * 获取所有菜单节点
     */
    public List<ResourceDto> menuNodeList() {
        List<ResourceEntity> entities = baseMapper.resourceDtoTreeList();
        //对菜单权重排序
        entities.sort(comparator);

        return getChildren("0", entities);
    }

    @Override
    public List<ResourceDto> menuNodeListByUserId(String userId, Integer button) {
        UserRoleEntity userRoleEntity = userRoleService.getOne(new QueryWrapper<UserRoleEntity>().lambda().eq(UserRoleEntity::getUserId, userId).select(UserRoleEntity::getId, UserRoleEntity::getRoleId));

        List<ResourceEntity> entities = baseMapper.getResourceTreeByRoleId(userRoleEntity.getRoleId(), button);
        //对菜单权重排序
        entities.sort(comparator);

        return getChildren("0", entities);
    }


    /**
     * 获取子菜单
     *
     * @param entities
     * @return
     */
    private List<ResourceDto> getChildren(String parentId, List<ResourceEntity> entities) {

        if (parentId == null) {
            menus.clear();
            entities.forEach(r -> {
                if (StringUtils.isEmpty(r.getParentId())) {
                    menus.add(toDto(r, entities));
                }
            });
        } else {
            List<ResourceDto> children = new ArrayList<>();
            entities.forEach(r -> {
                if (StringUtils.equals(parentId, r.getParentId())) {
                    children.add(toDto(r, entities));
                }
            });
            return children;
        }

        return menus;
    }

    private ResourceDto toDto(ResourceEntity r, List<ResourceEntity> entities) {
        ResourceDto dto = JSONObject.parseObject(r.getConfigure(), ResourceDto.class);
        if (null == dto) {
            dto = new ResourceDto();
        }
        dto.setName(r.getName());
        dto.setId(r.getId());
        dto.setParentId(r.getParentId());
        dto.setSort(r.getSort());
        dto.setUsed(r.getUsed());
        dto.setItems(getChildren(r.getId(), entities));
        return dto;
    }

    /**
     * Resources排序工具类
     */
    private class ResourceSortComparator implements Comparator<ResourceEntity> {
        @Override
        public int compare(ResourceEntity o1, ResourceEntity o2) {
            if (o1 != null && o2 != null) {
                if (o1.getSort() != null && o2.getSort() != null) {
                    return o2.getSort().compareTo(o1.getSort());
                } else if (o1.getSort() != null && o2.getSort() == null) {
                    return -1;
                } else if (o1.getSort() == null && o2.getSort() != null) {
                    return 1;
                }
            }
            return 0;
        }
    }

    private Comparator<ResourceEntity> comparator = new ResourceSortComparator();

    private List<ResourceDto> menus = new ArrayList<>();

    @Autowired
    private UserRoleService userRoleService;
}
