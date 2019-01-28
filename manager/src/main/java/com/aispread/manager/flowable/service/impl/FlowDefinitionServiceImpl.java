package com.aispread.manager.flowable.service.impl;

import com.aispread.manager.flowable.dto.FlowDefinitionListPage;
import com.aispread.manager.flowable.entity.FlowDefinitionEntity;
import com.aispread.manager.flowable.mapper.FlowDefinitionMapper;
import com.aispread.manager.flowable.service.FlowDefinitionService;
import com.aispread.manager.flowable.service.FlowUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.sf.ehcache.management.ManagementService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 流程定义表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2018-10-24
 */
@Service
public class FlowDefinitionServiceImpl extends ServiceImpl<FlowDefinitionMapper, FlowDefinitionEntity> implements FlowDefinitionService {

    @Override
    public FlowDefinitionListPage<FlowDefinitionEntity> list(FlowDefinitionListPage page) {
        if (StringUtils.isNotBlank(page.getName())) {
            page.setName("%" + page.getName() + "%");
        }
        return baseMapper.list(page,page.getOrdId(),page.getName(),page.getStatus(),page.getStartTime(),page.getEndTime());
    }
}
