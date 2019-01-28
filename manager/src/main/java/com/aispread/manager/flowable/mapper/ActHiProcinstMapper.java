package com.aispread.manager.flowable.mapper;

import com.aispread.manager.flowable.entity.ActHiProcinstEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2019-01-09
 */
public interface ActHiProcinstMapper extends BaseMapper<ActHiProcinstEntity> {

    /**
     * 获取已申请的任务数量
     */
    public Integer getApplyTaskCount(@Param("userId") String userId);

    /**
     * 获取已办任务数量
     */
    public Integer getDoneTaskCOunt(@Param("userId") String userId);


}
