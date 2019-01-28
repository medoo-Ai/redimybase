package com.redimybase.manager.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redimybase.framework.model.datamodel.ztree.Ztree;
import com.redimybase.manager.security.entity.ResourceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
@Mapper
public interface ResourceMapper extends BaseMapper<ResourceEntity> {

    List<String> getResKeyList(String userId);
    List<String> getResNameList(String userId);
    List<ResourceEntity> getResourceByUserId(@Param("userId") String userId,@Param("button") Integer button);
    List<ResourceEntity> getResourceByRoleId(@Param("roleId") String roleId);
    List<Ztree> resourceTreeList();

    List<ResourceEntity> resourceDtoTreeList();
}
