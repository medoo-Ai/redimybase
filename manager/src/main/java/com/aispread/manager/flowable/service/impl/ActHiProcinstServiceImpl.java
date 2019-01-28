package com.aispread.manager.flowable.service.impl;

import com.aispread.manager.flowable.entity.ActHiProcinstEntity;
import com.aispread.manager.flowable.mapper.ActHiProcinstMapper;
import com.aispread.manager.flowable.service.ActHiProcinstService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-01-09
 */
@Service
public class ActHiProcinstServiceImpl extends ServiceImpl<ActHiProcinstMapper, ActHiProcinstEntity> implements ActHiProcinstService {

    @Override
    public Integer getApplyTaskCount(String userId) {
        return baseMapper.getApplyTaskCount(userId);
    }

    @Override
    public Integer getDoneTaskCount(String userId) {
        return baseMapper.getDoneTaskCOunt(userId);
    }

}
