package com.aispread.manager.unit.service.impl;

import com.aispread.manager.model.entity.DeviceModelEntity;
import com.aispread.manager.model.service.UnitService;
import com.aispread.manager.unit.entity.UnitEntity;
import com.aispread.manager.unit.entity.UnitEntity.Status;
import com.aispread.manager.unit.mapper.UnitMapper;
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
 * 设备从表-计量单位 服务实现类
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-29
 */
@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, UnitEntity> implements UnitService {
  @Override
  public boolean removeById(Serializable id) {
    UnitEntity byId = packageGetById(id);
    byId.setStatus(UnitEntity.Status.删除);
    return updateById(byId);
  }

  @Override
  public boolean removeByIds(Collection<? extends Serializable> idList) {
    idList.stream().forEach(id ->{
      UnitEntity byId = packageGetById(id);
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
  public UnitEntity packageGetById(Serializable id)throws BusinessException {
    UnitEntity byId = getById(id);
    if(byId == null){
      throw new BusinessException(R.失败,"无效ID");
    }
    return byId;
  }

  @Override
  public UnitEntity getById(Serializable id) {
    QueryWrapper<UnitEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.and( q -> q.ne("status", UnitEntity.Status.删除));
    queryWrapper.and( q -> q.eq("id", id));
    return getOne(queryWrapper);
  }

  @Override
  public boolean save(UnitEntity entity) {
    /*唯一性校验*/
    QueryWrapper<UnitEntity> queryWrapper = new QueryWrapper<UnitEntity>();
    queryWrapper.and( q -> q.ne("status", UnitEntity.Status.删除));
    queryWrapper.and( q -> q.eq("name",entity.getName()));
    if(getOne(queryWrapper) != null){
      throw new BusinessException(R.失败,"单位名称(name)值已存在:"+entity.getName());
    }
    entity.setCreateAt(new Date());
    entity.setCreateBy(SecurityUtil.getCurrentUserId());
    entity.setStatus(Status.启用);
    return super.save(entity);
  }

  @Override
  public boolean updateById(UnitEntity entity) {
    /*唯一性校验*/
    QueryWrapper<UnitEntity> queryWrapper = new QueryWrapper<UnitEntity>();
    queryWrapper.and( q -> q.ne("status", UnitEntity.Status.删除));
    queryWrapper.and( q -> q.eq("name",entity.getName()));
    queryWrapper.and( q -> q.notIn("id",entity.getId()));
    if(getOne(queryWrapper) != null){
      throw new BusinessException(R.失败,"修改后的单位名称(name)值已存在:"+entity.getName());
    }

    entity.setUpdateAt(new Date());
    entity.setUpdateBy(SecurityUtil.getCurrentUserId());
    return super.updateById(entity);
  }

  @Override
  public IPage<UnitEntity> page(IPage<UnitEntity> page,
      Wrapper<UnitEntity> queryWrapper) {
    return super.page(page, queryWrapper);
  }

  /**
   * 封装的list方法,list中会过滤掉数据状态为已删除的数据
   * @param queryWrapper
   * @return
   */
  public List<UnitEntity> packageList(QueryWrapper<UnitEntity> queryWrapper){
    if(queryWrapper!=null){
      queryWrapper.and( q -> q.ne("status", UnitEntity.Status.删除));
    }
    return super.list(queryWrapper);
  }
}
