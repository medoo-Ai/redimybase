package com.aispread.manager.integrate.service.impl;

import com.aispread.manager.integrate.entity.SystemIntegrateEntity;
import com.aispread.manager.integrate.mapper.SystemIntegrateMapper;
import com.aispread.manager.integrate.service.SystemIntegrateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统集成表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-01-26
 */
@Service
public class SystemIntegrateServiceImpl extends ServiceImpl<SystemIntegrateMapper, SystemIntegrateEntity> implements SystemIntegrateService {

    @Override
    public List<SystemIntegrateEntity> queryByUserId(String userId) {
        return baseMapper.queryByUserId(userId);
    }
}
