package com.aispread.manager.announcement.service.impl;

import com.aispread.manager.announcement.entity.AnnouncementEntity;
import com.aispread.manager.announcement.entity.AnnouncementEntity.Model;
import com.aispread.manager.announcement.entity.AnnouncementEntity.Status;
import com.aispread.manager.announcement.entity.AnnouncementListDTO;
import com.aispread.manager.announcement.entity.AnnouncementQueryDTO;
import com.aispread.manager.announcement.entity.AnnouncementSaveDTO;
import com.aispread.manager.announcement.mapper.AnnouncementMapper;
import com.aispread.manager.announcement.service.AnnouncementService;
import com.aispread.manager.banner.entity.AppBannerEntity;
import com.aispread.manager.banner.service.impl.AppBannerServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.manager.security.entity.dto.TaskMainDTO;
import com.redimybase.security.utils.SecurityUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 通知公告 服务实现类
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-26
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, AnnouncementEntity> implements
    AnnouncementService {

  @Autowired
  AppBannerServiceImpl appBannerService;

  @Override
  public boolean removeById(Serializable id) {
    AnnouncementEntity byId = packageGetById(id);
    if(!byId.getReleaseUser().equals(SecurityUtil.getCurrentUserId())){
      throw new BusinessException(R.失败,"您不可以操作其他人发布的通告.");
    }
    /*状态校验*/
    if(byId.getStatus() == Status.审核中){
      throw new BusinessException(R.失败,"当前状态不可修改通告数据!");
    }
    byId.setStatus(Status.已删除);
    return updateById(byId);
  }


  @Override
  public boolean removeByIds(Collection<? extends Serializable> idList) {
    idList.stream().forEach(id -> {
      AnnouncementEntity byId = packageGetById(id);
      byId.setStatus(0);
      updateById(byId);
    });
    return true;
  }

  @Override
  public AnnouncementEntity getById(Serializable id){
    QueryWrapper<AnnouncementEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.and( q -> q.ne("status", Status.已删除));
    queryWrapper.and( q -> q.eq("id", id));
    return getOne(queryWrapper);
  }

  @Override
  public boolean save(AnnouncementEntity entity) {
    if(entity != null && entity.getCreateTime() == null){
      entity.setCreateTime(new Date());
      entity.setCreatorId(SecurityUtil.getCurrentUserId());
      entity.setReleaseTime(entity.getCreateTime());
      entity.setReleaseUser(entity.getCreatorId());
      entity.setStatus(Status.审核中);
    }
    return super.save(entity);
  }


  @Override
  public boolean updateById(AnnouncementEntity entity) {
    entity.setUpdateTime(new Date());
    entity.setUpdaterId(SecurityUtil.getCurrentUserId());
    return super.updateById(entity);
  }

  /**
   * 封装的 getById
   * @param id
   * @return
   * @throws BusinessException
   */
  public AnnouncementEntity packageGetById(Serializable id) throws BusinessException{
    AnnouncementEntity byId = getById(id);
    if(byId == null){
      throw new BusinessException(R.失败,"无效ID");
    }
    return byId;
  }

  /**
   * 保存轮播图
   * 轮播图类型默认状态为已发布不需要工作流
   * @param dto
   * @return
   */
  public boolean saveAppBanner(AnnouncementSaveDTO dto){
    /*校验*/
    if(StringUtils.isBlank(dto.getAttachmentId())){
      throw new BusinessException(R.失败,"图片附件ID不能为空.");
    }

    /*保存轮播图*/
    AppBannerEntity appBannerEntity = new AppBannerEntity();
    appBannerEntity.setName(dto.getTitle());
    appBannerEntity.setCreateTime(new Date());
    appBannerEntity.setSort(dto.getImgSort());
    appBannerEntity.setUrl(dto.getAttachmentId());
    appBannerService.save(appBannerEntity);

    /*关联轮播图*/
    AnnouncementEntity entity = new AnnouncementEntity();
    entity.setTitle(dto.getTitle());
    entity.setAttachmentId(dto.getAttachmentId());
    entity.setStatus(Status.已发布);
    entity.setModel(Model.图片信息);
    entity.setReleaseUser(SecurityUtil.getCurrentUserId());
    entity.setReleaseTime(new Date());
    entity.setCreateTime(entity.getReleaseTime());
    entity.setCreatorId(entity.getReleaseUser());
    entity.setAppBannerId(appBannerEntity.getId());
    save(entity);

    return true;
  }

  /**
   * 更新轮播图
   * @param dto
   * @return
   */
  public boolean updateAppBanner(AnnouncementSaveDTO dto) {

    AnnouncementEntity byId = packageGetById(dto.getId());
    Integer model = byId.getModel();
    if(model == dto.getModel() && model == Model.图片信息){
      if(StringUtils.isBlank(dto.getAppBannerId())){
        throw new BusinessException(R.失败,"appBannerId(图片ID)不能为空.");
      }
      AppBannerEntity appBannerEntity = new AppBannerEntity();
      appBannerEntity.setUrl(dto.getAttachmentId());
      appBannerEntity.setSort(dto.getImgSort());
      appBannerEntity.setName(dto.getTitle());
      appBannerEntity.setId(dto.getAppBannerId());
      return appBannerService.updateById(appBannerEntity);
    }else if(dto.getModel() == Model.图片信息){
      /*由公司公告切换为图片信息*/
      /*校验*/
      /*if(StringUtils.isBlank(dto.getAppBannerId())){
        throw new BusinessException(R.失败,"appBannerId(图片ID)不能为空.");
      }*/
      if(StringUtils.isBlank(dto.getAttachmentId())){
        throw new BusinessException(R.失败,"attachmentId(轮播图片附件ID)不能为空.");
      }
      if(byId.getStatus() == Status.审核中
          || byId.getStatus() == Status.已发布
          || byId.getStatus() == Status.已删除 ){
        throw new BusinessException(R.失败,"当前状态不可修改通告数据!");
      }
      if(!byId.getReleaseUser().equals(SecurityUtil.getCurrentUserId())){
        throw new BusinessException(R.失败,"您不可以操作其他人发布的通告.");
      }
      /*保存轮播图*/
      AppBannerEntity appBannerEntity = new AppBannerEntity();
      appBannerEntity.setName(dto.getTitle());
      appBannerEntity.setCreateTime(new Date());
      appBannerEntity.setSort(dto.getImgSort());
      appBannerEntity.setUrl(dto.getAttachmentId());
      appBannerService.save(appBannerEntity);
      /*关联轮播图*/
      byId.setTitle(dto.getTitle());
      byId.setAttachmentId(dto.getAttachmentId());
      byId.setStatus(Status.已发布);
      byId.setModel(Model.图片信息);
      byId.setAppBannerId(appBannerEntity.getId());
      updateById(byId);
      /*清空原始公告字段*/
      baseMapper.switchToImg(dto.getId());
      return true;
    }
    return false;
  }

  /**
   * 封装的新增方法
   * @param dto
   * @return
   * @throws BusinessException
   */
  public boolean packageSave(AnnouncementSaveDTO dto) throws BusinessException{
    AnnouncementEntity entity = new AnnouncementEntity();
    entity.setId(dto.getId());
    entity.setTitle(dto.getTitle());
    entity.setModel(dto.getModel());
    entity.setContent(dto.getContent());
    entity.setAttachmentId(dto.getAttachmentId());
    entity.setAppBannerId(dto.getAppBannerId());
    /*校验*/
      if(StringUtils.isBlank(entity.getTitle())){
        throw new BusinessException(R.失败,"title(标题)不能为空.");
      }
      if(StringUtils.isBlank(entity.getContent())){
        throw new BusinessException(R.失败,"content(发布内容)不能为空.");
      }
      return  save(entity);
  }

  /**
   * 封装的修改方法
   * @param dto
   * @return
   * @throws BusinessException
   */
  public boolean packageUpdateById(AnnouncementSaveDTO dto) throws BusinessException{
    if(StringUtils.isBlank(dto.getId())){
      throw new BusinessException(R.失败,"ID不能为空");
    }
    AnnouncementEntity getById = packageGetById(dto.getId());
    Integer model = getById.getModel();
    if(model == Model.公司公告 && model == dto.getModel()){
      if(getById.getStatus() == Status.审核中
          || getById.getStatus() == Status.已发布
          || getById.getStatus() == Status.已删除 ){
        throw new BusinessException(R.失败,"当前状态不可修改通告数据!");
      }
      if(!getById.getReleaseUser().equals(SecurityUtil.getCurrentUserId())){
        throw new BusinessException(R.失败,"您不可以操作其他人发布的通告.");
      }
      AnnouncementEntity entity = new AnnouncementEntity();
      entity.setId(dto.getId());
      entity.setTitle(dto.getTitle());
      entity.setModel(dto.getModel());
      entity.setContent(dto.getContent());
      return updateById(entity);
    }
    else if(model == Model.图片信息){
      /*由图片信息切换为公司公告,删除原始app轮播图并且走保存公司公告的逻辑*/
      baseMapper.switchToAnnouncement(dto.getId());
      AnnouncementEntity entity = new AnnouncementEntity();
      entity.setId(dto.getId());
      entity.setTitle(dto.getTitle());
      entity.setModel(dto.getModel());
      entity.setContent(dto.getContent());
      entity.setStatus(Status.审核中);
      /*校验*/
      if(StringUtils.isBlank(entity.getTitle())){
        throw new BusinessException(R.失败,"title(标题)不能为空.");
      }
      if(StringUtils.isBlank(entity.getContent())){
        throw new BusinessException(R.失败,"content(发布内容)不能为空.");
      }
      if(StringUtils.isBlank(dto.getAppBannerId())){
        throw new BusinessException(R.失败,"appBannerId(轮播图片ID)不能为空.");
      }
      if(appBannerService.getById(dto.getAppBannerId()) == null){
        throw new BusinessException(R.失败,"无效的appBannerId(轮播图片ID).");
      }
      appBannerService.removeById(dto.getAppBannerId());
      return  updateById(entity);
    }
    return false;
  }

  /**
   * 分页列表
   * @param page
   * @param query
   * @return
   */
  public IPage<AnnouncementListDTO> getDTOList(IPage<TaskMainDTO> page ,AnnouncementQueryDTO query){
    return baseMapper.getDTOList(page,query);
  }

  /**
   * 获取明细
   * @param id
   * @return
   * @throws BusinessException
   */
  public AnnouncementListDTO getDTO(String id)throws BusinessException{
    AnnouncementEntity entity = packageGetById(id);
    return baseMapper.getDTO(entity.getId());
  }

  public boolean rePublish(String id) {
    AnnouncementEntity entity = packageGetById(id);
    if(!entity.getReleaseUser().equals(SecurityUtil.getCurrentUserId())){
      throw new BusinessException(R.失败,"您不可以操作其他人发布的通告.");
    }
    if(entity.getStatus() != Status.已驳回 && entity.getStatus() != Status.已下架){
      throw new BusinessException(R.失败,"当前状态不可重新发布.");
    }
    /*图片信息不可重新发布*/
    if(entity.getModel() == Model.图片信息){
      throw new BusinessException(R.失败,"图片信息不可重新发布");
    }

    if(entity.getStatus() == Status.审核中
        || entity.getStatus() == Status.已发布
        || entity.getStatus() == Status.已删除 ){
      throw new BusinessException(R.失败,"当前状态不可重新发布通告数据!");
    }
    if(!entity.getReleaseUser().equals(SecurityUtil.getCurrentUserId())){
      throw new BusinessException(R.失败,"您不可以发布操作其他人创建的通告.");
    }
    entity.setStatus(1);
    return updateById(entity);
  }

  public boolean lowerShelf(String id) throws BusinessException{
    AnnouncementEntity entity = packageGetById(id);
    /*图片信息不可重新发布*/
    if(entity.getModel() == Model.图片信息){
      throw new BusinessException(R.失败,"图片信息不可重新发布");
    }
    if(entity.getStatus() != Status.已发布){
      throw new BusinessException(R.失败,"当前状态下的通告不可下架.");
    }
    if(!entity.getReleaseUser().equals(SecurityUtil.getCurrentUserId())){
      throw new BusinessException(R.失败,"您不可以下架其他人发布的通告.");
    }
    entity.setStatus(Status.已下架);
    return updateById(entity);
  }

}
