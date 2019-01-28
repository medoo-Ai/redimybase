package com.aispread.manager.flowable.mapper;

import com.aispread.manager.flowable.dto.FlowDefinitionListPage;
import com.aispread.manager.flowable.entity.FlowDefinitionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 流程定义表 Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2018-10-24
 */
public interface FlowDefinitionMapper extends BaseMapper<FlowDefinitionEntity> {


    public FlowDefinitionListPage<FlowDefinitionEntity> list(FlowDefinitionListPage page, @Param("orgId") String orgId, @Param("name") String name, @Param("status") Integer status, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
