package com.aispread.manager.devicetype.service.impl;

import com.aispread.manager.devicetype.dto.DeviceTypeDTO;
import com.aispread.manager.devicetype.entity.DeviceTypeEntity;
import com.aispread.manager.devicetype.mapper.DeviceTypeMapper;
import com.aispread.manager.devicetype.service.DeviceTypeService;
import com.aispread.manager.unit.entity.UnitEntity;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.security.utils.SecurityUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备从表-设备类型 服务实现类
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-29
 */
@Service
public class DeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceTypeEntity> implements DeviceTypeService {

  @Override
  public boolean removeById(Serializable id) {
    DeviceTypeEntity byId = packageGetById(id);
    byId.setStatus(DeviceTypeEntity.Status.删除);
    return updateById(byId);
  }

  @Override
  public boolean removeByIds(Collection<? extends Serializable> idList) {
    idList.stream().forEach(id ->{
      DeviceTypeEntity byId = packageGetById(id);
      byId.setStatus(0);
      updateById(byId);
    });
    return true;
  }

  /**
   * 封装的 getById
   * @param id
   * @return
   * @throws BusinessException
   */
  public DeviceTypeEntity packageGetById(Serializable id)throws BusinessException{
    DeviceTypeEntity byId = getById(id);
    if(byId == null){
      throw new BusinessException(R.失败,"无效ID");
    }
    return byId;
  }



  @Override
  public DeviceTypeEntity getById(Serializable id) {
    QueryWrapper<DeviceTypeEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.and( q -> q.ne("status", DeviceTypeEntity.Status.删除));
    queryWrapper.and( q -> q.eq("id", id));
    return getOne(queryWrapper);
  }

  @Override
  public boolean save(DeviceTypeEntity entity) {
    /*唯一性校验*/
    QueryWrapper<DeviceTypeEntity> queryWrapper = new QueryWrapper<DeviceTypeEntity>();
    queryWrapper.and( q -> q.ne("status", UnitEntity.Status.删除));
    queryWrapper.and( q -> q.eq("name",entity.getName()));
    if(getOne(queryWrapper) != null){
      throw new BusinessException(R.失败,"设备类型名称(name)值已存在:"+entity.getName());
    }

    entity.setCreateAt(new Date());
    entity.setCreateBy(SecurityUtil.getCurrentUserId());
    entity.setStatus(DeviceTypeEntity.Status.启用);
    return super.save(entity);
  }

  @Override
  public boolean updateById(DeviceTypeEntity entity) {
    /*唯一性校验*/
    QueryWrapper<DeviceTypeEntity> queryWrapper = new QueryWrapper<DeviceTypeEntity>();
    queryWrapper.and( q -> q.ne("status", DeviceTypeEntity.Status.删除));
    queryWrapper.and( q -> q.eq("name",entity.getName()));
    queryWrapper.and( q -> q.notIn("id",entity.getId()));
    if(getOne(queryWrapper) != null){
      throw new BusinessException(R.失败,"修改后的设备类型名称(name)值已存在:"+entity.getName());
    }

    packageGetById(entity.getId());
    entity.setUpdateAt(new Date());
    entity.setUpdateBy(SecurityUtil.getCurrentUserId());
    return super.updateById(entity);
  }

  @Override
  public IPage<DeviceTypeEntity> page(IPage<DeviceTypeEntity> page,
      Wrapper<DeviceTypeEntity> queryWrapper) {
    return super.page(page, queryWrapper);
  }

  /**
   * 封装的list方法,list中会过滤掉数据状态为已删除的数据
   * @param queryWrapper
   * @return
   */
  public List<DeviceTypeEntity> packageList(QueryWrapper<DeviceTypeEntity> queryWrapper){
    if(queryWrapper!=null){
      queryWrapper.and( q -> q.ne("status", DeviceTypeEntity.Status.删除));
    }
    return super.list(queryWrapper);
  }

  /**
   * 树结构列表
   * @param id
   * @return
   */
  public List<DeviceTypeDTO> getTypeTreeList(String id){
    List<DeviceTypeDTO> dtoList = new ArrayList<DeviceTypeDTO>();
    QueryWrapper<DeviceTypeEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.and(q -> q.isNull("parent"));
    if(StringUtils.isNotBlank(id)){
      queryWrapper.and(q -> q.eq("id",id));
    }
    List<DeviceTypeEntity> list = packageList(queryWrapper);
    for(DeviceTypeEntity entity:list){
      DeviceTypeDTO dto = new DeviceTypeDTO();
      dto.setId(entity.getId());
      dto.setName(entity.getName());
      dtoList.add(dto);
    }
    return getSubTypeTrre(dtoList);
  }

  /**
   * 递归方法-查询所有下级类型
   * @param dtoList
   * @return
   */
  public List<DeviceTypeDTO> getSubTypeTrre(List<DeviceTypeDTO> dtoList){
    for(DeviceTypeDTO dto:dtoList){
      QueryWrapper<DeviceTypeEntity> queryWrapper = new QueryWrapper<DeviceTypeEntity>();
      queryWrapper.and( q -> q.eq("parent", dto.getId()));
      List<DeviceTypeEntity> subList = packageList(queryWrapper);
      List<DeviceTypeDTO> list = new ArrayList<DeviceTypeDTO>();
      for(DeviceTypeEntity sub:subList){
        DeviceTypeDTO typeDTO = new DeviceTypeDTO();
        typeDTO.setId(sub.getId());
        typeDTO.setName(sub.getName());
        list.add(typeDTO);
      }
      if(subList != null && subList.size()>0){
        dto.setSubType(getSubTypeTrre(list));;
      }
    }
    return dtoList;
  }

  /**
   * 递归方法:获取一个设备类型下的所有子类型(包含本身)-平级
   * @param entity
   * @param result
   * @return
   */
  public List<DeviceTypeEntity> getSubTypeList(DeviceTypeEntity entity,List<DeviceTypeEntity> result){
    result.add(entity);
    /*查询直接子类型*/
    QueryWrapper<DeviceTypeEntity> queryWrapper = new QueryWrapper<DeviceTypeEntity>();
    queryWrapper.and( q -> q.eq("parent", entity.getId()));
    List<DeviceTypeEntity> subList = packageList(queryWrapper);
    for(DeviceTypeEntity sub:subList){
      getSubTypeList(sub,result);
    }
    return result;
  }

}
