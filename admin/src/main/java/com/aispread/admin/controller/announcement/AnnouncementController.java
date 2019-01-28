package com.aispread.admin.controller.announcement;


import com.aispread.manager.announcement.entity.AnnouncementEntity;
import com.aispread.manager.announcement.entity.AnnouncementEntity.Model;
import com.aispread.manager.announcement.entity.AnnouncementQueryDTO;
import com.aispread.manager.announcement.entity.AnnouncementSaveDTO;
import com.aispread.manager.announcement.mapper.AnnouncementMapper;
import com.aispread.manager.announcement.service.impl.AnnouncementServiceImpl;
import com.aispread.manager.banner.entity.AppBannerEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.BaseController;
import com.redimybase.manager.security.entity.dto.TaskMainDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 通知公告 前端控制器
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-26
 */

@RestController
@RequestMapping("/announcement")
@Api(tags="通知公告")
public class AnnouncementController extends BaseController<String, AnnouncementEntity, AnnouncementMapper, AnnouncementServiceImpl> {

  @Autowired
  AnnouncementServiceImpl service;

  @Override
  protected AnnouncementServiceImpl getService() {
    return service;
  }

  @PostMapping("/saveOrUpdate")
  @ApiOperation(value = "保存通知公告",notes = "支持新增/保存")
  public R<?> save(AnnouncementSaveDTO dto){

    /*校验*/
    if(dto.getModel() == null){
      throw new BusinessException(R.失败,"model(发布模块)不能为空.");
    }
    if(StringUtils.isBlank(dto.getId())){
      if(dto.getModel() == Model.公司公告){
        return new R<>(service.packageSave(dto));
      }else if(dto.getModel() == Model.图片信息){
        return new R<>(service.saveAppBanner(dto));
      }else{
        throw new BusinessException(R.失败,"无效参数model,取值范围[0,1]");
      }
    }else{
      if(dto.getModel() == Model.图片信息){
        return new R<>(service.updateAppBanner(dto));
      }else{
        return new R<>(service.packageUpdateById(dto));
      }
    }
  }

  @GetMapping("getDetailById")
  @ApiOperation(value="获取通知明细")
  public  R<?> getDetailById(String id){
    return new R<>(service.getDTO(id));
  }



  @GetMapping("getList")
  @ApiOperation(value="获取通知公告列表")
  public  R<?> getList(AnnouncementQueryDTO query){
    if(query.getP_NO() == null){
      query.setP_NO(1);
    }
    if(query.getP_SIZE() == null){
      query.setP_SIZE(10);
    }
    Page<TaskMainDTO> page = new Page<TaskMainDTO>();
    page.setCurrent(query.getP_NO());
    page.setSize(query.getP_SIZE());
    if (StringUtils.isNotBlank(query.getOrderBy())) {
      if (query.isAsc()) {
        page.setAsc(query.getOrderBy());
      } else {
        page.setDesc(query.getOrderBy());
      }
    }
    return new R<>(service.getDTOList(page,query));
  }

  @PostMapping("/rePublish")
  @ApiOperation(value="重新发布",notes = "图片信息不可重新发布")
  public  R<?> rePublish(String id){
    return new R<>(service.rePublish(id));
  }

  @PostMapping("/lowerShelf")
  @ApiOperation(value="下架",notes = "图片信息不可下架,如需删除直接调用删除接口")
  public  R<?> lowerShelf(String id){
    return new R<>(service.lowerShelf(id));
  }


}

