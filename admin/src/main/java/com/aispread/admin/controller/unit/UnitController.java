package com.aispread.admin.controller.unit;


import com.aispread.manager.unit.entity.UnitEntity;
import com.aispread.manager.unit.entity.UnitEntity.Status;
import com.aispread.manager.unit.mapper.UnitMapper;
import com.aispread.manager.unit.service.impl.UnitServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.web.BaseController;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 计量单位 前端控制器
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-29
 */
@RestController
@RequestMapping("/unit")
public class UnitController extends BaseController<String, UnitEntity, UnitMapper, UnitServiceImpl> {

  @Autowired
  private UnitServiceImpl service;

  @Override
  protected UnitServiceImpl getService() {
    return service;
  }

  @Override
  public Object query(HttpServletRequest request) {
    TableModel<UnitEntity> model = new TableModel<>();
    Page<UnitEntity> page = (Page<UnitEntity>) buildPageRequest(request);
    if (page == null) {
      page = new Page<>(1, 8);
    }
    QueryWrapper<UnitEntity> queryWrapper = buildWrapper(getQueryColumn(request), getQueryValue(request));
    if (null == queryWrapper) {
      queryWrapper = new QueryWrapper<>();
    }
    queryWrapper.and(i -> i.ne("status", Status.删除));
    IPage iPage = getService().page(page, buildPageWrapper(queryWrapper, getQueryKey(request), getQuerySearch(request)));
    model.setData(iPage);
    return model;
  }

}

