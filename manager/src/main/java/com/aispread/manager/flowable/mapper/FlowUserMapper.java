package com.aispread.manager.flowable.mapper;

import com.aispread.manager.flowable.entity.FlowUserEntity;
import com.aispread.manager.flowable.dto.FlowNodeUserDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2018-10-24
 */
public interface FlowUserMapper extends BaseMapper<FlowUserEntity> {

    @Select("select t1.id,t1.type,t1.value,t1.sort,t1.node_id,t2.user_name from t_flow_user t1 left join t_user t2 on t1.`value`=t2.id where t1.node_id=#{nodeId}")
    public List<FlowNodeUserDTO> getFlowUserByNodeId(@Param("nodeId") String nodeId);
}
