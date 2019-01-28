package com.redimybase.manager.security.mapper;

import com.redimybase.manager.security.entity.OrgEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户组织表 Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2018-11-23
 */
public interface OrgMapper extends BaseMapper<OrgEntity> {

    public List<OrgEntity> orgDtoTreeList();

    public List<OrgEntity> orgUserDtoTreeList();
}
