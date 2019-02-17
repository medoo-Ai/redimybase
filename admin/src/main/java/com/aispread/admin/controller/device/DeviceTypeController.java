package com.aispread.admin.controller.device;


import com.aispread.manager.devicetype.entity.DeviceTypeEntity;
import com.aispread.manager.devicetype.mapper.DeviceTypeMapper;
import com.aispread.manager.devicetype.service.impl.DeviceTypeServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 设备从表-设备类型 前端控制器
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-29
 */
@Api(tags = "设备类型")
@RestController
@RequestMapping("/device-type")
public class DeviceTypeController  extends BaseController<String, DeviceTypeEntity, DeviceTypeMapper, DeviceTypeServiceImpl> {

  @Autowired
  private DeviceTypeServiceImpl service;

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
    queryWrapper.and(i -> i.ne("status", DeviceTypeEntity.Status.删除));
    IPage iPage = getService().page(page, buildPageWrapper(queryWrapper, getQueryKey(request), getQuerySearch(request)));
    model.setData(iPage);
    return model;
  }

  @Override
  public void beforeSave(DeviceTypeEntity entity) {
    super.beforeSave(entity);
  }

  /**
   * 获取设备类型列表-树结构
   * @param parentId 指定类型ID,不指定默认返回所有顶级类型(顶级类型的子类型会包含在顶级类型里面)
   * @return
   */
  @ApiOperation(value = "获取设备类型列表-树结构")
  @GetMapping(value = "getDeviceTypeTreeList")
  public R<?> getDeviceTypeTreeList(String parentId){
    return  new R<>(service.getTypeTreeList(parentId));
  }

  /**
   * 获取某设备下的所有子设备-平级,包含当前设备
   * @param id
   * @return
   */
  @ApiOperation(value = "获取某设备下的所有子设备-平级,包含当前设备")
  public R<?> getSubTypeList(String id){
    return new R<>(service.getSubTypeList(service.packageGetById(id),new ArrayList<DeviceTypeEntity>()));
  }
}

