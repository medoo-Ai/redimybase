package com.redimybase.manager.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.manager.security.entity.DeviceEntity;
import com.redimybase.manager.security.entity.TaskMainEntity;
import com.redimybase.manager.security.entity.TaskMainEntity.Status;
import com.redimybase.manager.security.entity.TaskMainEntity.TaskStatus;
import com.redimybase.manager.security.entity.TaskReadStatus;
import com.redimybase.manager.security.entity.TaskSubEntity;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.entity.dto.TaskMainDTO;
import com.redimybase.manager.security.entity.dto.TaskSubDTO;
import com.redimybase.manager.security.mapper.TaskMainMapper;
import com.redimybase.manager.security.service.TaskMainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.security.utils.SecurityUtil;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协同任务 服务实现类
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-10
 */
@Service
public class TaskMainServiceImpl extends ServiceImpl<TaskMainMapper, TaskMainEntity> implements
    TaskMainService {

  @Autowired
  private TaskSubServiceImpl taskSubService;

  @Autowired
  private TaskReadStatusServiceImpl taskReadservice;

  @Autowired
  private UserServiceImpl userService;


  /**
   * 只更新主任务
   * @param entity
   * @return
   */
  @Override
  public boolean updateById(TaskMainEntity entity) {
    entity.setUpdateTime(new Date());
    UserEntity currentUser = SecurityUtil.getCurrentUser();
    entity.setReviserId(currentUser.getId());
    if(entity.isRelease()){
      entity.setTaskStatus(TaskStatus.进行中);
      entity.setReleaseTime(new Date());
    }
    return super.updateById(entity);
  }

  @Override
  public boolean removeById(Serializable id) {
    TaskMainEntity byId = getById(id);
    byId.setStatus(0);
    return updateById(byId);
  }

  @Override
  public boolean removeByIds(Collection<? extends Serializable> idList) {
    idList.stream().forEach(id -> {
      TaskMainEntity byId = getById(id);
      byId.setStatus(0);
      updateById(byId);
    });
    return true;
  }

  @Override
  public IPage<TaskMainEntity> page(IPage<TaskMainEntity> page,
      Wrapper<TaskMainEntity> queryWrapper) {
    return super.page(page, queryWrapper);
  }

  public IPage<TaskMainDTO> getTaskMainList(IPage<TaskMainDTO> page,
      TaskMainDTO query) {
    IPage<TaskMainDTO> result = baseMapper.getTaskMainList(page, query);
    /*获取和当前用户相关的所有主任务 Records*/
    List<TaskMainDTO> relateTaskMainDtoList =  getRelatedMainTaskList(result.getRecords());
    /*count*/
    QueryWrapper<TaskMainEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.and(i -> i.eq("status", TaskMainEntity.Status.启用).or().eq("status", DeviceEntity.Status.禁用));
    /*备份分页相关数据*/
    long pg_no = page.getCurrent();
    long pg_size = page.getSize();
    page.setCurrent(1L);
    page.setSize(9999999);
    IPage<TaskMainDTO> totalMainEntityList = baseMapper.getTaskMainList(page, query);
    List<TaskMainDTO> totalRelateTaskMainDtoList =  getRelatedMainTaskList(totalMainEntityList.getRecords());
    result.setTotal(totalRelateTaskMainDtoList.size());
    /*还原分页相关数据*/
    page.setCurrent(pg_no);
    page.setSize(pg_size);

    result.setRecords(relateTaskMainDtoList);
    return result;
  }

  /**
   * 获取和当前用户相关的所有主任务
   * @param records
   * @return
   */
  private List<TaskMainDTO> getRelatedMainTaskList(List<TaskMainDTO> records) {

    /*相关主任务ID汇总*/
    List<String> relatedMainIds = new ArrayList<>();
    String currentUserId = SecurityUtil.getCurrentUserId();
    for(TaskMainDTO main:records){
      /*如果发起人为当前用户直接判定为相关*/
      if(currentUserId.equals(main.getInitiator())){
        relatedMainIds.add(main.getId());
        continue;
      }
      /*否则遍历所有子任务,同样根据发起人/执行人判定相关状态*/
      QueryWrapper<TaskSubEntity> queryWrapper = new QueryWrapper<>();
      queryWrapper.and(i -> i.eq("parent",main.getId()));
      List<TaskSubEntity> list = taskSubService.list(queryWrapper);
      if(judgeRelate(list)){
        relatedMainIds.add(main.getId());
      }
    }
    /*去除无关主任务*/
    java.util.Iterator<TaskMainDTO> iterator =  records.iterator();
    while(iterator.hasNext()){
      TaskMainDTO item = iterator.next();
      if(!relatedMainIds.contains(item.getId())){
        iterator.remove();
      }
    }
    return records;
  }

  /**
   * 判定当前主任务的所有子任务中是否有创建人/执行人为当前用户
   * @param list
   */
  private boolean judgeRelate(List<TaskSubEntity> list){
    String currentUserId = SecurityUtil.getCurrentUserId();
    /*为避免无用的遍历,先遍历所有本级点后再遍历子节点*/
    for(TaskSubEntity sub:list){
      if(currentUserId.equals(sub.getCreatorId()) || currentUserId.equals(sub.getExecutiveId())){
        return true;
      }
    }
    /*此时本级节点全部无关,遍历下级节点*/
    for(TaskSubEntity sub:list){
      QueryWrapper<TaskSubEntity> queryWrapper = new QueryWrapper<>();
      queryWrapper.and(i -> i.eq("parent",sub.getId()));
      List<TaskSubEntity> sublist = taskSubService.list(queryWrapper);
      if(sublist != null && sublist.size() != 0){
        if(judgeRelate(sublist)){
          return true;
        };
      }
    }
    return false;
  }

  public TaskMainDTO getDTOById(String id) throws BusinessException{
    TaskMainDTO dtoById = baseMapper.getDTOById(id);
    if(dtoById != null) {
      /*判断已读未读状态*/
      boolean read = false;
      try {
        QueryWrapper<TaskReadStatus> queryWrapper = new QueryWrapper<TaskReadStatus>();
        queryWrapper.and(q -> q.eq("tasksub_id",dtoById.getId()));
        queryWrapper.and(q -> q.eq("user_id",SecurityUtil.getCurrentUserId()));
        read = taskReadservice.getOne(queryWrapper).isReadStatu();
      } catch (Exception e) {
      }
      dtoById.setRead(read);
      dtoById.setTaskSubList(getSubEntityList(dtoById.getId()));
    }else{
      /*throw new BusinessException(R.失败,"找不到该实体.");*/
      return null;
    }
    List<TaskSubDTO> taskSubList = dtoById.getTaskSubList();
    /*设置节点的已读状态*/
    ArrayList<String>  endNode = new ArrayList<String>();
    initEndNodeReadStatus(taskSubList,endNode);
    /*根据末级节点状态设置父级已读状态*/
    setParentReadStatus(dtoById,endNode);
    return dtoById;
  }
  public void setParentReadStatus(TaskMainDTO dto,ArrayList<String> endNode){
    for(String id:endNode){
      /*从数据库中获取所有父级*/
      ArrayList<String> parentList = new ArrayList<String>();
      String parentId = id;
      TaskSubEntity byId = null;
      try {
        byId = taskSubService.getById(parentId);
      } catch (Exception e) {
        e.printStackTrace();
      }
      while(byId != null){
        parentId = byId.getParent();
        if(StringUtils.isNotBlank(parentId)){
          parentList.add(parentId);
        }
        try {
          byId = null;
          byId = taskSubService.getById(parentId);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if(parentList.size()>0){
        dto.setRead(false);
      }
      processListReadStatus(dto.getTaskSubList(),parentList);
    }
  }
  private void processListReadStatus(List<TaskSubDTO> list,ArrayList<String> parentList){
    for(int i=0;i<list.size();i++){
      TaskSubDTO sub = list.get(i);
      if(parentList.contains(sub.getId())){
        sub.setRead(false);
      }
      if(sub.getTaskSubList() != null && sub.getTaskSubList().size() !=0){
        processListReadStatus(sub.getTaskSubList(),parentList);
      }
    }
  }




  private void initEndNodeReadStatus(List<TaskSubDTO> taskSubList,ArrayList<String> endNode){
    for(TaskSubDTO dto:taskSubList){
      List<TaskSubDTO> subList = dto.getTaskSubList();
        QueryWrapper<TaskReadStatus> queryWrapper = new QueryWrapper<TaskReadStatus>();
        queryWrapper.and(i -> i.eq("tasksub_id",dto.getId()));
        queryWrapper.and(i -> i.eq("user_id",SecurityUtil.getCurrentUserId()));
        boolean read = false;
        try {
          read = taskReadservice.getOne(queryWrapper).isReadStatu();
        } catch (Exception e) {
        }
        dto.setRead(read);
        if(!read){
          endNode.add(dto.getId());
        }
        initEndNodeReadStatus(subList,endNode);
    }
  }

  /**
   * 设置当前任务为已读,不区分主任务和明细任务
   * @param id
   * @return
   */
  public R<?> read(String id){
    QueryWrapper<TaskReadStatus> queryWrapper = new QueryWrapper<TaskReadStatus>();
    queryWrapper.and(q -> q.eq("tasksub_id",id));
    queryWrapper.and(q -> q.eq("user_id",SecurityUtil.getCurrentUserId()));
    TaskReadStatus one = taskReadservice.getOne(queryWrapper);
    if(one == null){
      TaskReadStatus readStatus = new TaskReadStatus();
      readStatus.setReadStatu(true);
      readStatus.setUserId(SecurityUtil.getCurrentUserId());
      readStatus.setTasksubId(id);
      return new R<>(taskReadservice.save(readStatus));
    }else{
      one.setReadStatu(true);
      return new R<>(taskReadservice.updateById(one));
    }
  }

  public R<?> simpleDetail(String id) throws BusinessException {
    /*判断ID类型*/
    TaskMainDTO dtoById = getDTOById(id);
    if(dtoById != null){
      QueryWrapper<TaskSubEntity> queryWrapper = new QueryWrapper<>();
      queryWrapper.and(i -> i.eq("parent",id));
      TaskSubDTO dto = new TaskSubDTO();
      dto.setParent(id);
      List<TaskSubDTO> list = taskSubService.list(dto);
      dtoById.setTaskSubList(list);
      return new R<>(dtoById);
    }else{
      TaskSubDTO dto = taskSubService.getDto(id);
      TaskSubDTO query = new TaskSubDTO();
      query.setParent(id);
      List<TaskSubDTO> list = taskSubService.list(query);
      dto.setTaskSubList(list);
      return new R<>(dto);
    }

  }
  /**
   * 发布任务
   * @param id
   * @return
   * @throws BusinessException
   */
  @Override
  public boolean release(String id) throws BusinessException {
    TaskMainEntity byId = super.getById(id);
    if(byId == null){
      throw new BusinessException(R.失败,"无效ID");
    }
    if(0 == byId.getTaskStatus()){
      byId.setTaskStatus(TaskStatus.进行中);
      byId.setReleaseTime(new Date());
    }else{
      throw new BusinessException(R.失败,"该任务不处于可发布状态");
    }
    return updateById(byId);
  }

  /**
   * 主任务-完成
   * @param id
   * @return
   * @throws BusinessException
   */
  @Override
  public boolean complete(String id) throws BusinessException {
    TaskMainEntity byId = getById(id);
    if(byId == null){
      throw new BusinessException(R.失败,"无效ID");
    }
    if(0 == byId.getTaskStatus()){
      throw new BusinessException(R.失败,"该任务处于发布状态不可完成");
    }else{
      byId.setTaskStatus(TaskStatus.完成);
    }
    return updateById(byId);
  }


  @Override
  public boolean updateSubTask(TaskSubEntity entity) throws BusinessException {
    entity.setStatus(null);
    entity.setParent(null);
    /*校验执行人*/
    if(StringUtils.isNotBlank(entity.getExecutiveId())){
      if(userService.getById(entity.getExecutiveId()) == null){
        throw new BusinessException(R.失败,"无效的执行人ID:" + entity.getExecutiveId());
      }
    }
    taskSubService.updateById(entity);
    TaskSubDTO subDTO = new TaskSubDTO();
    subDTO.setParent(entity.getId());

    /*旧的子任务ID*/
    ArrayList<String> oldSubIdList = new ArrayList<String>();
    taskSubService.list(subDTO).stream().forEach(i -> oldSubIdList.add(i.getId()));

    /*新的子任务ID集合*/
    ArrayList<String> newSubIdList = new ArrayList<String>();

    /*处理新增/更新的子任务*/
    for(TaskSubEntity sub:entity.getTaskSubList()){
      entity.setStatus(null);
      entity.setParent(null);
      /*校验执行人*/
      if(StringUtils.isNotBlank(entity.getExecutiveId())){
        if(userService.getById(entity.getExecutiveId()) == null){
          throw new BusinessException(R.失败,"无效的执行人ID:" + entity.getExecutiveId());
        }
      }
      if(StringUtils.isBlank(sub.getId())){
        sub.setParent(entity.getId());
        taskSubService.save(sub);
      }else{
        newSubIdList.add(sub.getId());
        taskSubService.updateById(sub);
      }
    }
    /*处理删除的子任务*/
    List<String> list = getReduceaListThanbList(newSubIdList, oldSubIdList);
    taskSubService.removeByIds(list);
    return true;
  }

  /**
   * 递归方法,更新父级任务状态
   * id 子任务ID
   * @param
   * @return
   *//*
  private boolean updateParentTaskStatus(String id) throws BusinessException{
    ArrayList<Integer> status = new ArrayList<Integer>();
    TaskSubEntity taskSubEntity  = null;
    判断父级类型
    boolean isMainTask = true;
    try {
      taskSubEntity = taskSubService.getById(taskSubService.getById(id).getParent());
      isMainTask =  taskSubEntity != null?false:true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    QueryWrapper<TaskSubEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.and(i -> i.eq("parent",taskSubService.getById(id).getParent() ));
    List<TaskSubEntity> list = taskSubService.list(queryWrapper);
    list.stream().forEach(sub ->{
      if(!status.contains(sub.getTaskStatus())){
        status.add(sub.getTaskStatus());
      }
    });
    Integer parentStatus = -1;

        if(isMainTask){
          if(status.size() == 1 && status.get(0) == TaskStatus.完成 ){
            parentStatus = TaskMainEntity.TaskStatus.完成;
          }else{
            parentStatus = TaskMainEntity.TaskStatus.进行中;
          }
          TaskMainEntity mainEntity = getById(taskSubService.getById(id).getParent());
          if(mainEntity.getTaskStatus() == TaskStatus.未发布){
            throw new BusinessException(R.失败,"该任务的顶级任务处于未发布状态,不可修改");
          }
          mainEntity.setTaskStatus(parentStatus);
          updateById(mainEntity);
        }else{
          if(status.size() == 1 && status.get(0) == TaskSubEntity.TaskStatus.完成 ){
            parentStatus = TaskSubEntity.TaskStatus.完成;
          }else{
            parentStatus = TaskSubEntity.TaskStatus.进行中;
          }
          TaskSubEntity subEntity = taskSubService.getById(taskSubService.getById(id).getParent());
          subEntity.setTaskStatus(parentStatus);
          taskSubService.updateById(subEntity);
          updateParentTaskStatus(subEntity.getId());
      }

    return true;
  }*/


  @Override
  public boolean save(TaskMainEntity entity) {
    UserEntity currentiUser = SecurityUtil.getCurrentUser();
    if(StringUtils.isBlank(entity.getId())){
      entity.setCreateTime(new Date());
      entity.setCreatorId(currentiUser.getId());
      entity.setInitiator(currentiUser.getId());
      entity.setStatus(Status.启用);
      entity.setTaskStatus(TaskStatus.未发布);
      if(entity.isRelease()){
        entity.setTaskStatus(TaskStatus.进行中);
        entity.setReleaseTime(new Date());
      }
      if(entity.getPriority() == null){entity.setPriority(0);}
      if(entity.getImportance() == null){entity.setImportance(0);}
      super.save(entity);
    }else{
      /*更新主任务*/
      updateById(entity);
    }
    if(entity.getTaskSubList() != null){
      /*保存/更新子任务*/
      TaskSubEntity sub = new TaskSubEntity();
      sub.setCreateTime(entity.getCreateTime());
      sub.setCreatorId(entity.getCreatorId());
      sub.setParent(entity.getId());
      sub.setStatus(TaskSubEntity.Status.启用);
      sub.setId(entity.getId());
      ArrayList<String> newIdList = new ArrayList<String>();
      ArrayList<String> oldIdList = new ArrayList<String>();
      getOldSubIdList(entity.getId(),oldIdList);
      saveSubEntityList(entity.getTaskSubList(),sub,newIdList);
      processDelSub(entity,newIdList,oldIdList);
    }
    return true;
  }


  private void processDelSub(TaskMainEntity entity,ArrayList<String> newIdList,ArrayList<String>oldIdList) {

    List<String> list = getReduceaListThanbList(newIdList, oldIdList);
    taskSubService.removeByIds(list);
  }

  /**
   * 判断元素element是否是sourceList列表中的一个子元素
   * @param sourceList 源列表
   * @param element 待判断的包含元素
   * @return 包含返回 true，不包含返回 false
   */
  private <E> boolean myListContains(List<E> sourceList, E element) {
    if (sourceList == null || element == null){
      return false;
    }
    if (sourceList.isEmpty()){
      return false;
    }
    for (E tip : sourceList){
      if(element.equals(tip)){
        return true;
      }
    }
    return false;
  }

  /**
   * 计算列表aList相对于bList的减少的情况，兼容任何类型元素的列表数据结构
   * @param aList 本列表
   * @param bList 对照列表
   * @return 返回减少的元素组成的列表
   */
  public <E> List<E> getReduceaListThanbList(List<E> aList, List<E> bList){
    List<E> reduceaList = new ArrayList<E>();
    for (int i = 0; i < bList.size(); i++){
      if(!myListContains(aList, bList.get(i))){
        reduceaList.add(bList.get(i));
      }
    }
    return reduceaList;
  }


  private void getOldSubIdList(String id,ArrayList<String> oldIdList) {
    TaskSubDTO subDTO = new TaskSubDTO();
    subDTO.setParent(id);
    List<TaskSubDTO> list = taskSubService.list(subDTO);
    for(int i=0;i<list.size();i++){
      oldIdList.add(list.get(i).getId());
      TaskSubDTO entity = list.get(i);
      getOldSubIdList(entity.getId(),oldIdList);
    }
  }

//  private ArrayList<TaskSubEntity> getDelSubTask(List<TaskSubEntity> taskSubList) {
//    return null;
//  }
//
//  private ArrayList<TaskSubEntity> getUpdateSubTask(List<TaskSubEntity> taskSubList) {
//    return null;
//  }
//
//  private ArrayList<TaskSubEntity> getAddSubTask(List<TaskSubEntity> taskSubList) {
//    taskSubList
//  }

//  @Override
//  public Page<TaskMainDTO>  getTaskMainList(Page<TaskMainDTO> page,TaskMainDTO query) {
//    return baseMapper.getTaskMainList(page, query);
//  }





  private void saveSubEntityList(List<TaskSubEntity> list,TaskSubEntity parent,ArrayList<String> subIdList){
    for(int i=0;i<list.size();i++){
      TaskSubEntity sub = list.get(i);
      if(StringUtils.isBlank(sub.getId())){
        sub.setCreateTime(new Date());
        sub.setCreatorId(SecurityUtil.getCurrentUserId());
        sub.setParent(parent.getId());
        sub.setStatus(1);
        taskSubService.save(sub);
        /*新建的任务设置本人已读*/
        try {
          TaskReadStatus readStatus = new TaskReadStatus();
          readStatus.setTasksubId(sub.getId());
          readStatus.setReadStatu(true);
          readStatus.setUserId(SecurityUtil.getCurrentUserId());
          taskReadservice.save(readStatus);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }else{
        sub.setReviserId(SecurityUtil.getCurrentUserId());
        sub.setUpdateTime(new Date());
        taskSubService.updateById(sub);
        subIdList.add(sub.getId());
      }
      if(sub.getTaskSubList() != null && sub.getTaskSubList().size() != 0){
        saveSubEntityList(sub.getTaskSubList(),sub,subIdList);
      }
    }
  }

//  @Override
//  public TaskMainEntity getById(Serializable id) {
//    TaskMainEntity mainEntity = super.getById(id);
//    if(mainEntity != null) {
//      mainEntity.setTaskSubList(getSubEntityList(mainEntity.getId()));
//    }
//    return mainEntity;
//  }

  /**
   * 递归赋值子任务列表
   * @param parentId
   * @return
   */
  private List<TaskSubDTO> getSubEntityList(String parentId){
    if(StringUtils.isBlank(parentId)){return null;}
    TaskSubDTO subDTO = new TaskSubDTO();
    subDTO.setParent(parentId);
    List<TaskSubDTO> list = taskSubService.list(subDTO);
    for(int i=0;i<list.size();i++){
      TaskSubDTO entity = list.get(i);
      /*判断已读未读状态*/
      boolean read = false;
      try {
        QueryWrapper<TaskReadStatus> queryWrapper = new QueryWrapper<TaskReadStatus>();
        queryWrapper.and(q -> q.eq("tasksub_id",entity.getId()));
        queryWrapper.and(q -> q.eq("user_id",SecurityUtil.getCurrentUserId()));
        read = taskReadservice.getOne(queryWrapper).isReadStatu();
      } catch (Exception e) {
      }
      entity.setRead(read);
      entity.setTaskSubList(getSubEntityList(entity.getId()));
    }
    return list;
  }

  @Override
  public boolean isRead(String id) {
    return false;
  }
}
