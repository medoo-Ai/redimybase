package com.aispread.admin.controller.inventory;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.common.util.CheckParametersUtil;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.security.entity.DeviceEntity;
import com.redimybase.manager.security.entity.InventoryEntity;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.entity.dto.InventoryUpdateDTO;
import com.redimybase.manager.security.mapper.InventoryMapper;
import com.redimybase.manager.security.service.impl.DeviceServiceImpl;
import com.redimybase.manager.security.service.impl.DeviceTypeServiceImpl;
import com.redimybase.manager.security.service.impl.InventoryServiceImpl;
import com.redimybase.security.utils.SecurityUtil;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 设备库存表 前端控制器
 * 默认删除       -禁用
 * 默认批量删除   -禁用
 * 默认更新       -禁用
 *
 * 新增和修改库存量的操作在由入库出库时调用,其余时机不应该操作库存
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-07
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController extends
    TableController<String, InventoryEntity, InventoryMapper, InventoryServiceImpl> {

  @Autowired
  private InventoryServiceImpl service;
  @Autowired
  private DeviceTypeServiceImpl typeService;
  @Autowired
  private DeviceServiceImpl deviceService;
  
  @Override
  protected InventoryServiceImpl getService() {
    return service;
  }

  @Override
  public Object query(HttpServletRequest request) {
    TableModel<InventoryEntity> model = new TableModel<>();
    Page<InventoryEntity> page = (Page<InventoryEntity>) buildPageRequest(request);
    if (page == null) {
      page = new Page<>(1, 8);
    }
    QueryWrapper<InventoryEntity> queryWrapper = buildWrapper(getQueryColumn(request), getQueryValue(request));

    if (null == queryWrapper) {
      queryWrapper = new QueryWrapper<>();
    }
    model.setData(getService().page(page, buildPageWrapper(queryWrapper, getQueryKey(request), getQuerySearch(request))));
    return model;
  }

  @Override
  public R<?> save(InventoryEntity entity) {
    /*校验*/
    if(StringUtils.isBlank(entity.getId())){
      try{
      CheckParametersUtil.getInstance()
          .put(entity.getType(),"type")
          .put(entity.getDevice(),"device")
          .put(entity.getType(),"type")
          .put(entity.getQty(),"qty")
          .checkParameter();
      }catch (Exception e){
        return new R(R.失败,e.getMessage());
      }
    }
    if(typeService.getById(entity.getType()) == null ){
      return new R(R.失败,"无效的设备类型ID");
    }
    if(deviceService.getById(entity.getDevice()) == null ){
      return new R(R.失败,"无效的设备ID");
    }
    QueryWrapper<DeviceEntity> deviceWrapper = new QueryWrapper<>();
    deviceWrapper.and(i -> i.eq("type", entity.getType()));
    if(deviceService.getOne(deviceWrapper) == null){
      return new R(R.失败,"设备和设备类型不匹配");
    }
    /*校验设备库存是否已存在*/
    QueryWrapper<InventoryEntity> inventoryWrapper = new QueryWrapper<>();
    inventoryWrapper.and(i -> i.eq("device", entity.getDevice()));
    if(service.getOne(inventoryWrapper) != null){
      return new R(R.失败,"该设备已存在库存信息,");
    }
    return new R<>(service.save(entity));
  }

  @Override
  public R<?> delete(String id) {
    return new R<>(R.失败,"不支持的操作");
  }

  @Override
  public R<?> deleteBatchIds(String ids) {
    return new R<>(R.失败, "不支持的操作");
  }


  /**
   * 获取设备库存
   * @param deviceId
   * @return
   */
  @PostMapping("getInventory")
  public R<?> getInventory(String deviceId) {
    if(StringUtils.isBlank(deviceId)){
      return new R<>(R.失败, "参数 deviceId 不能为空!");
    }
    if(deviceService.getById(deviceId) == null ){
      return new R(R.失败,"无效的设备ID");
    }
    QueryWrapper<InventoryEntity> inventoryWrapper = new QueryWrapper<>();
    inventoryWrapper.and(i -> i.eq("device", deviceId));
    InventoryEntity entity = service.getOne(inventoryWrapper);
    if(entity == null){
      return new R<>(R.失败, "该设备暂无库存信息");
    }
    return new R<>(entity.getQty());
  }

  /**
   * 更新设备库存
   * @param dto
   * @return
   */
  @PostMapping("updateInventory")
  public R<?> getInventory(InventoryUpdateDTO dto) {

    try {
      CheckParametersUtil.getInstance()
          .put(dto.getDeviceId(),"deviceId")
          .put(dto.getOffsetValue(),"offsetValue")
          .checkParameter();
    } catch (Exception e) {
      return new R<>(R.失败, e.getMessage());
    }
    boolean result = service.updateInventory(dto);
    if(result){
      return R.ok();
    }
    return R.fail();
  }

}

