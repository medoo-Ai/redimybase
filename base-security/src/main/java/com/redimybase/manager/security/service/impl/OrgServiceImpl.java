package com.redimybase.manager.security.service.impl;

import com.redimybase.manager.security.entity.OrgEntity;
import com.redimybase.manager.security.entity.dto.OrgTreeDto;
import com.redimybase.manager.security.mapper.OrgMapper;
import com.redimybase.manager.security.service.OrgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户组织表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2018-11-23
 */
@Service
public class OrgServiceImpl extends ServiceImpl<OrgMapper, OrgEntity> implements OrgService {


    /**
     * 获取所有组织节点
     */
    public List<OrgTreeDto> orgNodeList(String type) {
        List<OrgEntity> entities;
        if ("org".equals(type)) {
            entities = baseMapper.orgDtoTreeList();
        } else {
            entities = baseMapper.orgUserDtoTreeList();
        }

        return getChildren("0", entities);
    }

    /**
     * 获取子菜单
     *
     * @param entities
     * @return
     */
    private List<OrgTreeDto> getChildren(String parentId, List<OrgEntity> entities) {

        if (parentId == null) {
            menus.clear();
            entities.forEach(r -> {
                if (StringUtils.isEmpty(r.getParentId())) {
                    menus.add(toDto(r, entities));
                }
            });
        } else {
            List<OrgTreeDto> children = new ArrayList<>();
            entities.forEach(r -> {
                if (StringUtils.equals(parentId, r.getParentId())) {
                    children.add(toDto(r, entities));
                }
            });
            return children;
        }

        return menus;
    }

    private OrgTreeDto toDto(OrgEntity orgEntity, List<OrgEntity> entities) {
        OrgTreeDto dto = new OrgTreeDto();
        dto.setName(orgEntity.getName());
        dto.setId(orgEntity.getId());
        dto.setStatus(orgEntity.getStatus());
        dto.setCreateTime(orgEntity.getCreateTime());
        dto.setPid(orgEntity.getParentId());
        dto.setUpdateTime(orgEntity.getUpdateTime());
        dto.setParentName(orgEntity.getParentName());
        dto.setItems(getChildren(orgEntity.getId(), entities));
        return dto;
    }

    private List<OrgTreeDto> menus = new ArrayList<>();
}
