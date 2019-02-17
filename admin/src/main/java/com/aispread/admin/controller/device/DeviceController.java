package com.aispread.admin.controller.device;


import com.aispread.manager.device.entity.DeviceEntity;
import com.aispread.manager.device.entity.dto.DeviceQueryDto;
import com.aispread.manager.device.mapper.DeviceMapper;
import com.aispread.manager.device.service.impl.DeviceServiceImpl;
import com.aispread.manager.devicetype.dto.DeviceTypeDTO;
import com.aispread.manager.devicetype.service.impl.DeviceTypeServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.web.BaseController;
import com.redimybase.manager.security.entity.dto.TaskMainDTO;
import com.redimybase.manager.security.entity.dto.TaskMainQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 设备主表 前端控制器
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-29
 */
@Api(tags = "设备")
@RestController
@RequestMapping("/device")
public class DeviceController extends BaseController<String, DeviceEntity, DeviceMapper, DeviceServiceImpl> {

  @Autowired
  private DeviceServiceImpl service;

  @Autowired
  private DeviceTypeServiceImpl typeService;

  @Override
  protected DeviceServiceImpl getService() {
    return service;
  }

  @Override
  public Object query(HttpServletRequest request) {
    TableModel<DeviceEntity> model = new TableModel<>();
    Page<DeviceEntity> page = (Page<DeviceEntity>) buildPageRequest(request);
    if (page == null) {
      page = new Page<>(1, 8);
    }
    QueryWrapper<DeviceEntity> queryWrapper = buildWrapper(getQueryColumn(request), getQueryValue(request));
    if (null == queryWrapper) {
      queryWrapper = new QueryWrapper<>();
    }
    queryWrapper.and(i -> i.ne("status", DeviceEntity.Status.删除));
    IPage iPage = getService().page(page, buildPageWrapper(queryWrapper, getQueryKey(request), getQuerySearch(request)));
    model.setData(iPage);
    return model;
  }

  @PostMapping("getDeviceDtoList")
  @ApiOperation(value = "获取设备列表",notes = "请求/响应字段说明:"
      + "\n 请求和响应中公用字段:"
      + "\n &nbsp;&nbsp;name:设备名称"
      + "\n &nbsp;&nbsp;typeId:设备类型ID "
      + "\n &nbsp;&nbsp;typeName:设备类型名称 "
      + "\n &nbsp;&nbsp;parentTypeId:父级类型ID "
      + "\n &nbsp;&nbsp;parentTypeName:父级类型名称 "
      + "\n &nbsp;&nbsp;brandId:品牌ID "
      + "\n &nbsp;&nbsp;brandName:品牌名称"
      + "\n &nbsp;&nbsp;modelId:规格型号ID"
      + "\n &nbsp;&nbsp;modelName:规格型号名称"
      + "\n &nbsp;&nbsp;unitId:计量单位ID"
      + "\n &nbsp;&nbsp;unitName:计量单位名称"
      + "\n &nbsp;&nbsp;remark:备注"
      + "\n &nbsp;&nbsp;status:状态(0:删除,1:启用,2:禁用)"
      + "\n &nbsp;&nbsp;createAt:创建时间(该字段暂不支持过滤查询)"
      + "\n &nbsp;&nbsp;createBy:创建人ID"
      + "\n &nbsp;&nbsp;createName:创建人名称"
      + "\n &nbsp;&nbsp;updateAt:更新时间(该字段暂不支持过滤查询)"
      + "\n &nbsp;&nbsp;updateBy:更新人ID"
      + "\n &nbsp;&nbsp;updateName:更新人名称"
      + "\n 请求中的字段:"
      + "\n &nbsp;&nbsp;P_NO:页数"
      + "\n &nbsp;&nbsp;P_SIZE:数据大小"
      + "\n &nbsp;&nbsp;orderBy:排序字段"
      + "\n &nbsp;&nbsp;asc:是否顺序排序"
      + "" )
  public R<?> getDeviceDtoList(@RequestBody DeviceQueryDto query) {
    if(query.getP_NO() == null){
      query.setP_NO(1);
    }
    if(query.getP_SIZE() == null){
      query.setP_SIZE(10);
    }
    Page<DeviceQueryDto> page = new Page<DeviceQueryDto>();
    page.setCurrent(query.getP_NO());
    page.setSize(query.getP_SIZE());
    if (StringUtils.isNotBlank(query.getOrderBy())) {
      if (query.isAsc()) {
        page.setAsc(query.getOrderBy());
      } else {
        page.setDesc(query.getOrderBy());
      }
    }
    return new R<>(service.getDeviceDTOList(page,query));
  }

  @Override
  public R<?> save(DeviceEntity entity) {
    if(StringUtils.isBlank(entity.getId())){
      return new R<>(service.packageSave(entity));
    }else{
      service.packageGetById(entity.getId());
      return new R<>(service.updateById(entity));
    }
  }
}

