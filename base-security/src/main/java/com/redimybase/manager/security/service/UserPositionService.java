package com.redimybase.manager.security.service;

import com.redimybase.manager.security.entity.PositionEntity;
import com.redimybase.manager.security.entity.UserPositionEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户岗位关联表 服务类
 * </p>
 *
 * @author vim
 * @since 2019-01-06
 */
public interface UserPositionService extends IService<UserPositionEntity> {

    /**
     * 根据用户ID和组织ID获取职位
     */
    public List<PositionEntity> getPositionByUserIdAndOrgId(String userId, String orgId);
}
