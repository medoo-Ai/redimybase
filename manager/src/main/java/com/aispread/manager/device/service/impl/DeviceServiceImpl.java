package com.aispread.manager.device.service.impl;

import com.aispread.manager.brand.service.impl.DeviceBrandServiceImpl;
import com.aispread.manager.device.entity.DeviceEntity;
import com.aispread.manager.device.entity.DeviceEntity.Status;
import com.aispread.manager.device.entity.dto.DeviceDto;
import com.aispread.manager.device.entity.dto.DeviceQueryDto;
import com.aispread.manager.device.mapper.DeviceMapper;
import com.aispread.manager.device.service.DeviceService;
import com.aispread.manager.devicetype.service.impl.DeviceTypeServiceImpl;
import com.aispread.manager.model.service.impl.DeviceModelServiceImpl;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备主表 服务实现类
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-29
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, DeviceEntity> implements DeviceService {

  @Autowired
  private DeviceTypeServiceImpl typeService;

  @Autowired
  private DeviceBrandServiceImpl brandService;

  @Autowired
  private DeviceModelServiceImpl modelService;

  @Override
  public boolean removeById(Serializable id) {
    DeviceEntity byId = packageGetById(id);
    byId.setStatus(DeviceEntity.Status.删除);
    return updateById(byId);
  }

  @Override
  public boolean removeByIds(Collection<? extends Serializable> idList) {
    idList.stream().forEach(id ->{
      DeviceEntity byId = packageGetById(id);
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
  public DeviceEntity packageGetById(Serializable id)throws BusinessException {
    DeviceEntity byId = getById(id);
    if(byId == null){
      throw new BusinessException(R.失败,"无效ID");
    }
    return byId;
  }



  @Override
  public DeviceEntity getById(Serializable id) {
    QueryWrapper<DeviceEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.and( q -> q.ne("status", DeviceEntity.Status.删除));
    queryWrapper.and( q -> q.eq("id", id));
    return getOne(queryWrapper);
  }

  private void throwError(String msg)throws BusinessException{
    throw new BusinessException(R.失败,msg);
  }

  public boolean packageSave(DeviceEntity entity) throws BusinessException{

    /*非空*/
    if(StringUtils.isBlank(entity.getName())){
      throwError("设备名称(name)不能为空!");
    }
    if(StringUtils.isBlank(entity.getTypeId())){
      throwError("设备类型(typeId)不能为空!");
    }
    if(StringUtils.isBlank(entity.getBrandId())){
      throwError("品牌(typeId)不能为空!");
    }
    if(StringUtils.isBlank(entity.getModelId())){
      throwError("规格型号(modelId)不能为空!");
    }
    if(StringUtils.isBlank(entity.getUnitId())){
      throwError("单位(unitId)不能为空!");
    }

    /*合法性校验*/
    if(typeService.getById(entity.getTypeId()) == null){
      throwError("无效类型(typeId)值:"+entity.getTypeId());
    }
    if(typeService.getById(entity.getBrandId()) == null){
      throwError("无效品牌(brandId)值:"+entity.getBrandId());
    }
    if(typeService.getById(entity.getModelId()) == null){
      throwError("无效规格型号(modelId)值:"+entity.getModelId());
    }
    if(typeService.getById(entity.getUnitId()) == null){
      throwError("无效单位(unitId)值:"+entity.getUnitId());
    }

    /*唯一性校验*/
    QueryWrapper<DeviceEntity> queryWrapper = new QueryWrapper<DeviceEntity>();
    queryWrapper.and( q -> q.ne("status", DeviceEntity.Status.删除));
    queryWrapper.and( q -> q.eq("name",entity.getName()));
    if(getOne(queryWrapper) != null){
      throwError("设备名称(name)值已存在:"+entity.getName());
    }

    return save(entity);
  }


  @Override
  public boolean save(DeviceEntity entity) {
    entity.setCreateAt(new Date());
    entity.setCreateBy(SecurityUtil.getCurrentUserId());
    entity.setStatus(Status.启用);
    return super.save(entity);
  }

  @Override
  public boolean updateById(DeviceEntity entity) throws BusinessException{

    /*唯一性校验*/
    QueryWrapper<DeviceEntity> queryWrapper = new QueryWrapper<DeviceEntity>();
    queryWrapper.and( q -> q.ne("status", DeviceEntity.Status.删除));
    queryWrapper.and( q -> q.eq("name",entity.getName()));
    queryWrapper.and( q -> q.notIn("id",entity.getName()));
    if(getOne(queryWrapper) != null){
      throwError("修改后的设备名称(name)值已存在:"+entity.getName());
    }
    if(true){
      throw new BusinessException(R.失败,"");
    }
    if(true){
      throw new RuntimeException();
    }

    entity.setUpdateAt(new Date());
    entity.setUpdateBy(SecurityUtil.getCurrentUserId());
    return super.updateById(entity);
  }

  @Override
  public IPage<DeviceEntity> page(IPage<DeviceEntity> page,
      Wrapper<DeviceEntity> queryWrapper) {
    return super.page(page, queryWrapper);
  }

  /**
   * 封装的list方法,list中会过滤掉数据状态为已删除的数据
   * @param queryWrapper
   * @return
   */
  public List<DeviceEntity> packageList(QueryWrapper<DeviceEntity> queryWrapper){
    if(queryWrapper!=null){
      queryWrapper.and( q -> q.ne("status", DeviceEntity.Status.删除));
    }
    return super.list(queryWrapper);
  }

  /**
   * 自定义分页列表
   * @param page
   * @param query
   * @return
   */
  public IPage<DeviceDto> getDeviceDTOList(IPage<DeviceQueryDto> page, DeviceQueryDto query) {
    return  baseMapper.getDeviceDTOList(page, query);
  }
}
