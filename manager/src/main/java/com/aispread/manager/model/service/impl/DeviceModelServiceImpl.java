package com.aispread.manager.model.service.impl;

import com.aispread.manager.device.entity.DeviceEntity;
import com.aispread.manager.model.entity.DeviceModelEntity;
import com.aispread.manager.model.mapper.ModelMapper;
import com.aispread.manager.model.service.DeviceModelService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.security.utils.SecurityUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备从表-规格/型号 服务实现类
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-29
 */
@Service("deviceModeServiceImpl")
public class DeviceModelServiceImpl extends ServiceImpl<ModelMapper, DeviceModelEntity> implements
    DeviceModelService {
  @Override
  public boolean removeById(Serializable id) {
    DeviceModelEntity byId = packageGetById(id);
    byId.setStatus(DeviceModelEntity.Status.删除);
    return updateById(byId);
  }

  @Override
  public boolean removeByIds(Collection<? extends Serializable> idList) {
    idList.stream().forEach(id ->{
      DeviceModelEntity byId = packageGetById(id);
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
  public DeviceModelEntity packageGetById(Serializable id)throws BusinessException{
    DeviceModelEntity byId = getById(id);
    if(byId == null){
      throw new BusinessException(R.失败,"无效ID");
    }
    return byId;
  }

  @Override
  public DeviceModelEntity getById(Serializable id) {
    QueryWrapper<DeviceModelEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.and( q -> q.ne("status", DeviceModelEntity.Status.删除));
    queryWrapper.and( q -> q.eq("id", id));
    return getOne(queryWrapper);
  }

  public boolean packageSave(DeviceModelEntity entity) throws BusinessException{
    /*唯一性校验*/
    QueryWrapper<DeviceModelEntity> queryWrapper = new QueryWrapper<DeviceModelEntity>();
    queryWrapper.and( q -> q.ne("status", DeviceModelEntity.Status.删除));
    queryWrapper.and( q -> q.in("name",entity.getName()));
    if(getOne(queryWrapper) != null){
      throw new BusinessException(R.失败,"设备名称(name)值已存在:"+entity.getName());
    }
      return save(entity);
  }

  @Override
  public boolean save(DeviceModelEntity entity) {
    entity.setCreateAt(new Date());
    entity.setCreateBy(SecurityUtil.getCurrentUserId());
    entity.setStatus(DeviceModelEntity.Status.启用);
    return super.save(entity);
  }

  @Override
  public boolean updateById(DeviceModelEntity entity) {
    /*唯一性校验*/
    QueryWrapper<DeviceModelEntity> queryWrapper = new QueryWrapper<DeviceModelEntity>();
    queryWrapper.and( q -> q.ne("status", DeviceModelEntity.Status.删除));
    queryWrapper.and( q -> q.eq("name",entity.getName()));
    queryWrapper.and( q -> q.notIn("id",entity.getId()));
    if(getOne(queryWrapper) != null){
      throw new BusinessException(R.失败,"设备名称(name)值已存在:"+entity.getName());
    }
    entity.setUpdateAt(new Date());
    entity.setUpdateBy(SecurityUtil.getCurrentUserId());
    return super.updateById(entity);
  }

  @Override
  public IPage<DeviceModelEntity> page(IPage<DeviceModelEntity> page,
      Wrapper<DeviceModelEntity> queryWrapper) {
    return super.page(page, queryWrapper);
  }

  /**
   * 封装的list方法,list中会过滤掉数据状态为已删除的数据
   * @param queryWrapper
   * @return
   */
  public List<DeviceModelEntity> packageList(QueryWrapper<DeviceModelEntity> queryWrapper){
    if(queryWrapper!=null){
      queryWrapper.and( q -> q.ne("status", DeviceModelEntity.Status.删除));
    }
    return super.list(queryWrapper);
  }
}
