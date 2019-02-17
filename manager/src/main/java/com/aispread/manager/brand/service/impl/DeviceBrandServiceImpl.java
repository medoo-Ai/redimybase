package com.aispread.manager.brand.service.impl;

import com.aispread.manager.brand.entity.DeviceBrandEntity;
import com.aispread.manager.brand.mapper.DeviceBrandMapper;
import com.aispread.manager.brand.service.DeviceBrandService;
import com.aispread.manager.device.entity.DeviceEntity.Status;
import com.aispread.manager.devicetype.entity.DeviceTypeEntity;
import com.aispread.manager.devicetype.service.impl.DeviceTypeServiceImpl;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备从表-品牌 服务实现类
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-29
 */
@Service
public class DeviceBrandServiceImpl extends ServiceImpl<DeviceBrandMapper, DeviceBrandEntity> implements
    DeviceBrandService {

  @Autowired
  private DeviceTypeServiceImpl typeService;

  @Override
  public boolean removeById(Serializable id) {
    DeviceBrandEntity byId = packageGetById(id);
    byId.setStatus(DeviceBrandEntity.Status.删除);
    return updateById(byId);
  }

  @Override
  public boolean removeByIds(Collection<? extends Serializable> idList) {
    idList.stream().forEach(id ->{
      DeviceBrandEntity byId = packageGetById(id);
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
  public DeviceBrandEntity packageGetById(Serializable id)throws BusinessException {
    DeviceBrandEntity byId = getById(id);
    if(byId == null){
      throw new BusinessException(R.失败,"无效ID");
    }
    return byId;
  }


  @Override
  public DeviceBrandEntity getById(Serializable id) {
    QueryWrapper<DeviceBrandEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.and( q -> q.ne("status", DeviceBrandEntity.Status.删除));
    queryWrapper.and( q -> q.eq("id", id));
    return getOne(queryWrapper);
  }

  @Override
  public boolean save(DeviceBrandEntity entity) {
    /*唯一性校验*/
    QueryWrapper<DeviceBrandEntity> queryWrapper = new QueryWrapper<DeviceBrandEntity>();
    queryWrapper.and( q -> q.ne("status", DeviceBrandEntity.Status.删除));
    queryWrapper.and( q -> q.eq("name",entity.getName()));
    if(getOne(queryWrapper) != null){
      throw new BusinessException(R.失败,"品牌名称(name)值已存在:"+entity.getName());
    }
    entity.setCreateAt(new Date());
    entity.setCreateBy(SecurityUtil.getCurrentUserId());
    entity.setStatus(DeviceBrandEntity.Status.启用);
    return super.save(entity);
  }

  @Override
  public boolean updateById(DeviceBrandEntity entity) {
    /*唯一性校验*/
    QueryWrapper<DeviceBrandEntity> queryWrapper = new QueryWrapper<DeviceBrandEntity>();
    queryWrapper.and( q -> q.ne("status", DeviceBrandEntity.Status.删除));
    queryWrapper.and( q -> q.eq("name",entity.getName()));
    queryWrapper.and( q -> q.notIn("id",entity.getId()));
    if(getOne(queryWrapper) != null){
      throw new BusinessException(R.失败,"修改后的品牌名称(name)值已存在:"+entity.getName());
    }
    entity.setUpdateAt(new Date());
    entity.setUpdateBy(SecurityUtil.getCurrentUserId());
    return super.updateById(entity);
  }

  @Override
  public IPage<DeviceBrandEntity> page(IPage<DeviceBrandEntity> page,
      Wrapper<DeviceBrandEntity> queryWrapper) {
    return super.page(page, queryWrapper);
  }

  /**
   * 封装的list方法,list中会过滤掉数据状态为已删除的数据
   * @param queryWrapper
   * @return
   */
  public List<DeviceBrandEntity> packageList(QueryWrapper<DeviceBrandEntity> queryWrapper){
    if(queryWrapper!=null){
      queryWrapper.and( q -> q.ne("status", DeviceBrandEntity.Status.删除));
    }
    return super.list(queryWrapper);
  }

  public R<?> getCompleteList(String typeId) {
    List<DeviceBrandEntity> result = new ArrayList<DeviceBrandEntity>();
    DeviceTypeEntity deviceTypeEntity = typeService.packageGetById(typeId);
    List<DeviceTypeEntity> list = new ArrayList<DeviceTypeEntity>();
    List<DeviceTypeEntity> subTypeList = typeService.getSubTypeList(deviceTypeEntity, list);
    for(DeviceTypeEntity subType:subTypeList){
      QueryWrapper<DeviceBrandEntity> queryWrapper = new QueryWrapper<DeviceBrandEntity>();
      queryWrapper.and( q -> q.eq("type_id", subType.getId()));
      List<DeviceBrandEntity> trandList = packageList(queryWrapper);
      result.addAll(trandList);
    }
    return new R<>(result);
  }
}
