package com.aispread.admin.controller.announcement;


import com.aispread.manager.announcement.entity.AnnouncementEntity;
import com.aispread.manager.announcement.entity.AnnouncementEntity.Model;
import com.aispread.manager.announcement.entity.AnnouncementEntity.Status;
import com.aispread.manager.announcement.entity.AnnouncementListDTO;
import com.aispread.manager.announcement.entity.AnnouncementQueryDTO;
import com.aispread.manager.announcement.entity.AnnouncementSaveDTO;
import com.aispread.manager.announcement.mapper.AnnouncementMapper;
import com.aispread.manager.announcement.service.impl.AnnouncementServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.BaseController;
import com.aispread.manager.jointtask.entity.TaskMainDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    if(dto.getStatus() != null && dto.getStatus() != Status.草稿 && dto.getStatus() != Status.已发布 && dto.getStatus() != Status.已驳回 && dto.getStatus() != Status.已下架){
      throw new BusinessException(R.失败,"无效的公告状态,值范围[1:草稿,2:已发布,3:已驳回,4:已下架].");
    }
    if(dto.getIstop() != null && !"0".equals(dto.getIstop()) && !"1".equals(dto.getIstop())){
      throw new BusinessException(R.失败,"无效的置顶值,值范围[0:不置顶,1:置顶].");
    }
    if(dto.getIstop() == null){
      /*默认不置顶*/
      dto.setIstop("0");
    }
    if(StringUtils.isBlank(dto.getId())){
      if(Model.公司公告.equals(dto.getModel())){
        return new R<>(service.packageSave(dto));
      }else if(Model.图片公告.equals(dto.getModel())){
        return new R<>(service.saveAppBanner(dto));
      }else{
        throw new BusinessException(R.失败,"无效参数model,取值范围[0,1]");
      }
    }else{
      AnnouncementEntity byId = service.getById(dto.getId());
      if(Status.已发布.equals(byId.getStatus())){
        throw new BusinessException(R.失败,"不能修改已发布状态的公告!");
      }
      if(Model.图片公告.equals(byId.getModel())){
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
    Page<AnnouncementListDTO> page = new Page<AnnouncementListDTO>();
    page.setCurrent(query.getP_NO());
    page.setSize(query.getP_SIZE());
    if (StringUtils.isNotBlank(query.getOrderBy())) {
      if (query.isAsc()) {
        page.setAsc(query.getOrderBy());
      } else {
        page.setDesc(query.getOrderBy());
      }
    }else{
      /*默认:创建时间倒序 + 置顶倒序*/
      ArrayList<String> orderbys = new ArrayList<String>();
      orderbys.add("istop");
      orderbys.add("createTime");
      page.setDescs(orderbys);
    }

    return new R<>(service.getDTOList(page,query));
  }

  @PostMapping("/rePublish")
  @ApiOperation(value="重新发布",notes = "")
  public  R<?> rePublish(String id){
    return new R<>(service.rePublish(id));
  }

  @PostMapping("/lowerShelf")
  @ApiOperation(value="下架",notes = "")
  public  R<?> lowerShelf(String id){
    return new R<>(service.lowerShelf(id));
  }


}

