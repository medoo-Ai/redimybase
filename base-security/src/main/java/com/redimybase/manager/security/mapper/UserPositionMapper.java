package com.redimybase.manager.security.mapper;

import com.redimybase.manager.security.entity.PositionEntity;
import com.redimybase.manager.security.entity.UserPositionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户岗位关联表 Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2019-01-06
 */
public interface UserPositionMapper extends BaseMapper<UserPositionEntity> {

    /**
     * 根据用户ID和组织ID获取职位
     */
    @Select("select t1.name from t_position t1 left join t_user_position t2 on t1.id=t2.position_id where t2.user_id=#{userId} and t1.org_id=#{orgId}")
    public List<PositionEntity> getPositionByUserIdAndOrgId(@Param("userId") String userId, @Param("orgId") String orgId);
}
