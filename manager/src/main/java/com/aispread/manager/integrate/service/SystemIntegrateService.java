package com.aispread.manager.integrate.service;

import com.aispread.manager.integrate.entity.SystemIntegrateEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统集成表 服务类
 * </p>
 *
 * @author vim
 * @since 2019-01-26
 */
public interface SystemIntegrateService extends IService<SystemIntegrateEntity> {

    public List<SystemIntegrateEntity> queryByUserId(String userId);
}
