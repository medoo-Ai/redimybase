package com.aispread.admin.controller.device;

import com.aispread.manager.model.entity.DeviceModelEntity;
import com.aispread.manager.model.mapper.ModelMapper;
import com.aispread.manager.model.service.impl.DeviceModelServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.web.BaseController;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 设备从表-规格/型号 前端控制器
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-29
 */
@Api(tags = "设备规格型号")
@RestController
@RequestMapping("/device-model")
public class DeviceModelController extends BaseController<String, DeviceModelEntity, ModelMapper, DeviceModelServiceImpl> {

  @Autowired
  private DeviceModelServiceImpl service;

  @Override
  protected DeviceModelServiceImpl getService() {
    return service;
  }

  @Override
  public Object query(HttpServletRequest request) {
    TableModel<DeviceModelEntity> model = new TableModel<>();
    Page<DeviceModelEntity> page = (Page<DeviceModelEntity>) buildPageRequest(request);
    if (page == null) {
      page = new Page<>(1, 8);
    }
    QueryWrapper<DeviceModelEntity> queryWrapper = buildWrapper(getQueryColumn(request), getQueryValue(request));
    if (null == queryWrapper) {
      queryWrapper = new QueryWrapper<>();
    }
    queryWrapper.and(i -> i.ne("status", DeviceModelEntity.Status.删除));
    IPage iPage = getService().page(page, buildPageWrapper(queryWrapper, getQueryKey(request), getQuerySearch(request)));
    model.setData(iPage);
    return model;
  }
}

