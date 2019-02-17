package com.aispread.admin.controller.device;

import com.aispread.manager.brand.entity.DeviceBrandEntity;
import com.aispread.manager.brand.mapper.DeviceBrandMapper;
import com.aispread.manager.brand.service.impl.DeviceBrandServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.web.BaseController;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 设备从表-品牌 前端控制器
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-29
 */
@Api(tags = "设备品牌")
@RestController
@RequestMapping("/device-brand")
public class DeviceBrandController extends BaseController<String, DeviceBrandEntity, DeviceBrandMapper, DeviceBrandServiceImpl> {

  @Autowired
  private DeviceBrandServiceImpl service;

  @Override
  protected DeviceBrandServiceImpl getService() {
    return service;
  }

  @Override
  public Object query(HttpServletRequest request) {
    TableModel<DeviceBrandEntity> model = new TableModel<>();
    Page<DeviceBrandEntity> page = (Page<DeviceBrandEntity>) buildPageRequest(request);
    if (page == null) {
      page = new Page<>(1, 8);
    }
    QueryWrapper<DeviceBrandEntity> queryWrapper = buildWrapper(getQueryColumn(request), getQueryValue(request));
    if (null == queryWrapper) {
      queryWrapper = new QueryWrapper<>();
    }
    queryWrapper.and(i -> i.ne("status", DeviceBrandEntity.Status.删除));
    IPage iPage = getService().page(page, buildPageWrapper(queryWrapper, getQueryKey(request), getQuerySearch(request)));
    model.setData(iPage);
    return model;
  }

  /**
   * 获取某一设备类型下所有的品牌(包括该设备类型的所有子设备类型对应的品牌)
   * @param typeId
   * @return
   */
  @GetMapping(value = "getCompleteList")
  public R<?> getCompleteList(String typeId){
    return service.getCompleteList(typeId);
  }

}

