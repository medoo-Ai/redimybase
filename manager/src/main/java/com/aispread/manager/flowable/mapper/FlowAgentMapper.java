package com.aispread.manager.flowable.mapper;

import com.aispread.manager.flowable.entity.FlowAgentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 流程任务代理人表 Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2019-01-21
 */
public interface FlowAgentMapper extends BaseMapper<FlowAgentEntity> {

    @Select("select t1.user_id,t1.agent_id from t_flow_agent t1 left join t_flow_definition t2 on t1.definition_id=t2.id where t2.flow_definition_id=#{flowDefinitionId} and t1.user_id=#{userId}")
    public FlowAgentEntity fingByUserIdAndFlowDefinitionId(@Param("userId") String userId,@Param("flowDefinitionId") String flowDefinitionId);
}
