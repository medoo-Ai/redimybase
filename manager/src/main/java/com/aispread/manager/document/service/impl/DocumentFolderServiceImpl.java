package com.aispread.manager.document.service.impl;

import com.aispread.manager.document.dto.DocumentFolderTree;
import com.aispread.manager.document.entity.DocumentFolderEntity;
import com.aispread.manager.document.mapper.DocumentFolderMapper;
import com.aispread.manager.document.service.DocumentFolderService;
import com.aispread.manager.flowable.dto.FlowCategoryDTO;
import com.aispread.manager.flowable.entity.FlowDefinitionCategoryEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.framework.bean.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 文档目录表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2018-12-26
 */
@Service
public class DocumentFolderServiceImpl extends ServiceImpl<DocumentFolderMapper, DocumentFolderEntity> implements DocumentFolderService {

    /**
     * 获取子级
     */
    private List<DocumentFolderTree> getChildren(String parentId, List<DocumentFolderEntity> entities) {

        if (parentId == null) {
            menus.clear();
            entities.forEach(r -> {
                if (StringUtils.isEmpty(r.getParentId())) {
                    menus.add(toDto(r, entities));
                }
            });
        } else {
            List<DocumentFolderTree> children = new ArrayList<>();
            entities.forEach(r -> {
                if (StringUtils.equals(parentId, r.getParentId())) {
                    children.add(toDto(r, entities));
                }
            });
            return children;
        }

        return menus;
    }

    private DocumentFolderTree toDto(DocumentFolderEntity toDto, List<DocumentFolderEntity> entities) {
        DocumentFolderTree dto = new DocumentFolderTree();
        dto.setName(toDto.getName());
        dto.setId(toDto.getId());
        dto.setParentId(toDto.getParentId());
        dto.setSort(toDto.getSort());
        dto.setName(toDto.getName());
        dto.setItems(getChildren(toDto.getId(), entities));
        return dto;
    }

    private List<DocumentFolderTree> menus = new ArrayList<>();

    @Override
    public R<?> getFolderTree(String name) {
        List<DocumentFolderEntity> entities;
        if (StringUtils.isEmpty(name)) {
            entities = list(null);
        } else {
            entities = list(new QueryWrapper<DocumentFolderEntity>().like("name", "%" + name + "%"));
        }

        return new R<>(getChildren(null, entities));
    }
}
