package com.aispread.admin.controller.inventory;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.security.entity.DeviceEntity;
import com.redimybase.manager.security.entity.DeviceEntity.Status;
import com.redimybase.manager.security.entity.DeviceTypeEntity;
import com.redimybase.manager.security.entity.DeviceTypeEntity;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.mapper.DeviceMapper;
import com.redimybase.manager.security.mapper.DeviceTypeMapper;
import com.redimybase.manager.security.service.DeviceTypeService;
import com.redimybase.manager.security.service.impl.DeviceServiceImpl;
import com.redimybase.manager.security.service.impl.DeviceTypeServiceImpl;
import com.redimybase.security.utils.SecurityUtil;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 设备类型 前端控制器
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-07
 */
@RestController
@RequestMapping("/device-type")
public class DeviceTypeController extends
    TableController<String, DeviceTypeEntity, DeviceTypeMapper, DeviceTypeServiceImpl> {

  @Autowired
  DeviceTypeServiceImpl service;

  @Override
  protected DeviceTypeServiceImpl getService() {
    return service;
  }

  @Override
  public Object query(HttpServletRequest request) {
    TableModel<DeviceTypeEntity> model = new TableModel<>();
    Page<DeviceTypeEntity> page = (Page<DeviceTypeEntity>) buildPageRequest(request);
    if (page == null) {
      page = new Page<>(1, 8);
    }
    QueryWrapper<DeviceTypeEntity> queryWrapper = buildWrapper(getQueryColumn(request), getQueryValue(request));

    if (null == queryWrapper) {
      queryWrapper = new QueryWrapper<>();
    }
    queryWrapper.and(i -> i.eq("status", DeviceEntity.Status.启用).or().eq("status", DeviceEntity.Status.禁用));
    model.setData(getService().page(page, buildPageWrapper(queryWrapper, getQueryKey(request), getQuerySearch(request))));
    return model;
  }

  @Override
  public R<?> save(DeviceTypeEntity entity) {
    /*校验*/
    if(StringUtils.isBlank(entity.getId()) && StringUtils.isBlank(entity.getName())){
      return new R(R.失败,"设备名称不能为空");
    }
    if(!StringUtils.isBlank(entity.getId())){
      if(service.getById(entity.getId()) == null){
        return new R(R.失败,"无效的更新ID");
      }
    }
    return new R<>(super.save(entity));
  }

  @Override
  public R<?> delete(String id) {
    if(service.getById(id) == null){
      return new R<>(R.失败,"无效ID");
    }
    return new R<>(service.removeById(id));
  }
  @Override
  public R<?> detail(String id) {
    DeviceTypeEntity byId = service.getById(id);
    if(byId != null && !Status.删除.equals(byId.getStatus())){
      return new R<>(byId);
    }
    return new R<>(R.成功,"该实体不存在.");
  }

  @Override
  public R<?> deleteBatchIds(String ids) {
    /*校验ids中是否包含不存在的ID*/
    String[] idArr = StringUtils.split(ids, ",");
    for(String id:idArr){
      if(service.getById(id)==null){
        return new R(R.失败,"ID:" + id + "不存在!");
      }
    }
    return super.deleteBatchIds(ids);
  }
}

