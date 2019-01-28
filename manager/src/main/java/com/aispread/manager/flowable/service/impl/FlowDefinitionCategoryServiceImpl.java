package com.aispread.manager.flowable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.aispread.manager.flowable.entity.FlowDefinitionCategoryEntity;
import com.aispread.manager.flowable.dto.FlowCategoryDTO;
import com.aispread.manager.flowable.mapper.FlowDefinitionCategoryMapper;
import com.aispread.manager.flowable.service.FlowDefinitionCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 流程定义种类表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2018-10-24
 */
@Service
public class FlowDefinitionCategoryServiceImpl extends ServiceImpl<FlowDefinitionCategoryMapper, FlowDefinitionCategoryEntity> implements FlowDefinitionCategoryService {

    /**
     * 获取子级
     */
    private List<FlowCategoryDTO> getChildren(String parentId, List<FlowDefinitionCategoryEntity> entities) {

        if (parentId == null) {
            menus.clear();
            entities.forEach(r -> {
                if (StringUtils.isEmpty(r.getParentId())) {
                    menus.add(toDto(r, entities));
                }
            });
        } else {
            List<FlowCategoryDTO> children = new ArrayList<>();
            entities.forEach(r -> {
                if (StringUtils.equals(parentId, r.getParentId())) {
                    children.add(toDto(r, entities));
                }
            });
            return children;
        }

        return menus;
    }

    private FlowCategoryDTO toDto(FlowDefinitionCategoryEntity toDto, List<FlowDefinitionCategoryEntity> entities) {
        FlowCategoryDTO dto = new FlowCategoryDTO();
        dto.setName(toDto.getName());
        dto.setId(toDto.getId());
        dto.setParentId(toDto.getParentId());
        dto.setSort(toDto.getSort());
        dto.setName(toDto.getName());
        dto.setDescription(toDto.getDescription());
        dto.setItems(getChildren(toDto.getId(), entities));
        return dto;
    }

    private List<FlowCategoryDTO> menus = new ArrayList<>();

    @Override
    public R<?> getCategoryTree(String name) {
        List<FlowDefinitionCategoryEntity> entities;
        if (StringUtils.isEmpty(name)) {
            entities = list(null);
        } else {
            entities = list(new QueryWrapper<FlowDefinitionCategoryEntity>().like("name", "%" + name + "%"));
        }

        return new R<>(getChildren("0", entities));
    }
}
