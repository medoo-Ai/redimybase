package com.aispread.manager.flowable.service;

import com.aispread.manager.flowable.entity.ActHiProcinstEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vim
 * @since 2019-01-09
 */
public interface ActHiProcinstService extends IService<ActHiProcinstEntity> {


    /**
     * 获取已申请任务数量
     */
    public Integer getApplyTaskCount(String userId);

    /**
     * 获取已办任务数量
     */
    public Integer getDoneTaskCount(String userId);
}
