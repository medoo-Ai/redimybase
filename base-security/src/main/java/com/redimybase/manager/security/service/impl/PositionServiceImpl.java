package com.redimybase.manager.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.manager.security.entity.PositionEntity;
import com.redimybase.manager.security.entity.UserOrgEntity;
import com.redimybase.manager.security.entity.UserPositionEntity;
import com.redimybase.manager.security.mapper.PositionMapper;
import com.redimybase.manager.security.service.PositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.manager.security.service.UserOrgService;
import com.redimybase.manager.security.service.UserPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 岗位表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-01-06
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, PositionEntity> implements PositionService {

    @Override
    public String getLeadershipId(String userId) {
        //流程发起人所属部门的关联实体
        UserOrgEntity userOrgEntity = userOrgService.getOne(new QueryWrapper<UserOrgEntity>().eq("user_id",userId).select("org_id"));

        //部门负责人
        PositionEntity positionEntity = positionService.getOne(new QueryWrapper<PositionEntity>().eq("org_id", userOrgEntity.getOrgId()).eq("level_id", PositionEntity.Level.部门负责人));
        //部门负责人与岗位的关联表
        UserPositionEntity userPositionEntity = userPositionService.getOne(new QueryWrapper<UserPositionEntity>().eq("position_id", positionEntity.getId()).select("user_id"));
        return userPositionEntity.getUserId();
    }


    @Autowired
    private UserOrgService userOrgService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private UserPositionService userPositionService;
}
