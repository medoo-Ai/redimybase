package com.aispread.manager.flowable.mapper;

import com.aispread.manager.flowable.dto.FlowDistributeRecord;
import com.aispread.manager.flowable.entity.FlowDistributeTaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 流程分发任务表 Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2019-01-18
 */
public interface FlowDistributeTaskMapper extends BaseMapper<FlowDistributeTaskEntity> {


    @Select("select t1.id,t1.content,t1.attachment_id,t1.create_time,t2.user_name as assignee_name from t_flow_distribute_task t1 left join t_user t2 on t1.assignee =t2.id where t1.business_id=#{businessId} and t1.task_code=#{taskCode}")
    public List<FlowDistributeRecord> getRecord(@Param("businessId") String businessId,@Param("taskCode") String taskCode);

}
