package com.aispread.manager.integrate.mapper;

import com.aispread.manager.integrate.entity.SystemIntegrateEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 系统集成表 Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2019-01-26
 */
public interface SystemIntegrateMapper extends BaseMapper<SystemIntegrateEntity> {

    public List<SystemIntegrateEntity> queryByUserId(String userId);
}
