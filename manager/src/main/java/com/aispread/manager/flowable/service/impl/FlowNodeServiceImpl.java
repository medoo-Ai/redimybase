package com.aispread.manager.flowable.service.impl;

import com.aispread.manager.flowable.entity.FlowNodeEntity;
import com.aispread.manager.flowable.dto.FlowNodeTreeDTO;
import com.aispread.manager.flowable.mapper.FlowNodeMapper;
import com.aispread.manager.flowable.service.FlowNodeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.framework.bean.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 流程节点表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2018-10-24
 */
@Service
public class FlowNodeServiceImpl extends ServiceImpl<FlowNodeMapper, FlowNodeEntity> implements FlowNodeService {

    @Override
    public R<?> queryByDefinitionId(String definitionId, String type) {
        if (StringUtils.isNotBlank(type)) {
            return new R<>(getChildren("0", list(new QueryWrapper<FlowNodeEntity>().eq("definition_id", definitionId).in("type", StringUtils.split(type, ",")))));
        }
        return new R<>(getChildren("0", list(new QueryWrapper<FlowNodeEntity>().eq("definition_id", definitionId))));
    }

    /**
     * 获取子级
     */
    private List<FlowNodeTreeDTO> getChildren(String parentId, List<FlowNodeEntity> entities) {

        if (parentId == null) {
            menus.clear();
            entities.forEach(r -> {
                if (StringUtils.isEmpty(r.getParentId())) {
                    menus.add(toDto(r, entities));
                }
            });
        } else {
            List<FlowNodeTreeDTO> children = new ArrayList<>();
            entities.forEach(r -> {
                if (StringUtils.equals(parentId, r.getParentId())) {
                    children.add(toDto(r, entities));
                }
            });
            return children;
        }

        return menus;
    }

    private FlowNodeTreeDTO toDto(FlowNodeEntity toDto, List<FlowNodeEntity> entities) {
        FlowNodeTreeDTO dto = new FlowNodeTreeDTO();
        dto.setName(toDto.getName());
        dto.setId(toDto.getId());
        dto.setParentId(toDto.getParentId());
        dto.setTaskCode(toDto.getTaskCode());
        dto.setName(toDto.getName());
        dto.setType(toDto.getType());
        dto.setItems(getChildren(toDto.getId(), entities));
        return dto;
    }


    private List<FlowNodeTreeDTO> menus;

}
