package com.aispread.admin.controller.collaborationtask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.common.util.CheckParametersUtil;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.web.BaseController;
import com.redimybase.manager.security.entity.TaskMainEntity;
import com.redimybase.manager.security.entity.TaskMainEntity.Status;
import com.redimybase.manager.security.entity.TaskSubEntity;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.entity.dto.TaskMainDTO;
import com.redimybase.manager.security.entity.dto.TaskMainQuery;
import com.redimybase.manager.security.entity.dto.TaskSubDTO;
import com.redimybase.manager.security.mapper.TaskMainMapper;
import com.redimybase.manager.security.service.impl.TaskMainServiceImpl;
import com.redimybase.manager.security.service.impl.TaskSubServiceImpl;
import com.redimybase.manager.security.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 协同任务 前端控制器
 * </p>
 *
 * @author vim
 * @since 2019-01-10
 */
@RestController
@RequestMapping("/task-main")
@Api(tags = "协作任务")
public class TaskMainController extends BaseController<String, TaskMainEntity, TaskMainMapper, TaskMainServiceImpl> {
  /*查询表达式:
      Q_COLUMN=AND_字段1_EQ,AND_字段2_GE
      Q_VALUE=值1,值2
      */
  @Autowired
  private TaskMainServiceImpl service;
  @Autowired
  private UserServiceImpl userService;
  @Autowired
  private TaskSubServiceImpl subService;

  /**
   * 通用的查询方法
   * @param request
   * @return
   */
  @Override
  public Object query(HttpServletRequest request) {
    TableModel<TaskMainEntity> model = new TableModel<>();
    Page<TaskMainEntity> page = (Page<TaskMainEntity>) buildPageRequest(request);
    if (page == null) {
      page = new Page<>(1, 8);
    }
    QueryWrapper<TaskMainEntity> queryWrapper = buildWrapper(getQueryColumn(request), getQueryValue(request));
    if (null == queryWrapper) {
      queryWrapper = new QueryWrapper<>();
    }
    queryWrapper.and(i -> i.eq("status", TaskMainEntity.Status.启用).or().eq("status", TaskMainEntity.Status.禁用));
    IPage iPage = getService().page(page, buildPageWrapper(queryWrapper, getQueryKey(request), getQuerySearch(request)));
    model.setData(iPage);
    return model;
  }


  /**
   * 主任务列表
   * 1.只显示和当前账号有关的任务
   * @param query
   * @return
   */
  @PostMapping("getTaskList")
  @ApiOperation(value = "获取主任务列表")
  public R<?> getTaskList(TaskMainQuery query) {
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
    IPage<TaskMainDTO> taskMainList = service.getTaskMainList(page, query);
    List<TaskMainDTO> records = taskMainList.getRecords();
    records.stream().forEach( r -> r.setRead(service.getDTOById(r.getId()).getRead()));
    return new R<>(taskMainList);
  }


  /**
   * 通用的删除方法
   * @param id
   * @return
   */
  @Override
  public R<?> delete(String id) {
    TaskMainEntity byId = service.getById(id);
    byId.setStatus(Status.删除);
    return new R<>(service.updateById(byId));
  }

  /**
   * 根据子任务ID删除子任务(逻辑删除)
   * @param id
   * @return
   */
  @PostMapping("deleteSubById")
  @ApiOperation(value="根据子任务ID删除子任务(逻辑删除)")
  public R<?> deleteSubById(String id) {
    return new R<>(subService.removeById(id));
  }


  /**
   * 根据子任务ID集合批量删除子任务(逻辑删除)
   * @param idList
   * @return
   */
  @PostMapping("deleteSubByIdList")
  @ApiOperation(value="根据子任务ID集合批量删除子任务(逻辑删除)")
  public R<?> deleteSubById(Collection<? extends Serializable> idList) {
    return new R<>(subService.removeByIds(idList));
  }

  /**
   * 更新子任务
   * 注意:此方法需要传入当前任务和直接子任务的信息,只有当前任务没有子任务会认为是删掉了子任务
   * 有新增子任务或子任务的'任务完成描述'有变更时需要更新顶级任务的最后更新时间和更新人
   * @param entity
   * @return
   */
  @PostMapping("updateSubTask")
  @ApiOperation(value = "更新子任务", notes = "此方法需要传入当前任务和直接子任务的信息,只有当前任务没有子任务会认为是删掉了子任务,"
      + "有新增子任务或子任务的'任务完成描述'有变更时需要更新顶级任务的最后更新时间和更新人")
  public R<?> updateSubTask(@RequestBody TaskSubEntity entity) {
    /*清空直接子任务的子任务(如果有的话)*/
    if(entity != null){
      if(subService.getById(entity.getId()) == null){
        return new R<>(R.失败,"无效ID!");
      }
      List<TaskSubEntity> taskSubList = entity.getTaskSubList();
      for(TaskSubEntity sub:taskSubList){
        sub.setTaskSubList(null);
      }
    }
    return new R<>(service.updateSubTask(entity));
  }

  /**
   * 获取任务明细,包含当前任务和所有子任务
   * @param id
   * @return
   */
  @Override
  @ApiOperation(value = "获取任务明细",notes = "包含当前任务和所有子任务")
  public R<?> detail(String id) {
    TaskMainDTO byId = service.getDTOById(id);
    if(byId == null){
      return new R<>(R.失败,"该实体不存在.");
    }
    return new R<>(byId);
  }

  /**
   * 获取任务明细2- 只包含当前任务(主任务或子任务)和它的一级子任务
   * @param id
   * @return
   */
  @PostMapping("simpleDetail")
  @ApiOperation(value = "获取任务明细2",notes = "只包含当前任务(主任务或子任务)和它的一级子任务")
  public R<?> simpleDetail(String id) {
    return new R<>(service.simpleDetail(id));
  }


  @Override
  protected TaskMainServiceImpl getService() {
    return service;
  }

  /**
   * 发布主任务
   * @param id
   * @return
   */
  @PostMapping("release")
  @ApiOperation(value = "发布主任务")
  public R<?> release(String id) {
    return new R<>(service.release(id));
  }

  /**
   * 设置已读
   * @param id
   * @return
   */
  @PostMapping("read")
  @ApiOperation(value = "设置已读")
  public R<?> read(String id) {
    return new R<>(service.read(id));
  }

  /**
   * 设置主任务完成状态
   * @param id
   * @return
   */
  @PostMapping("complete")
  @ApiOperation(value = "设置主任务完成状态")
  public R<?> complete(String id) {
    return new R<>(service.complete(id));
  }


  /**
   * 只更新主任务
   * @param entity
   * @return
   */
  @PostMapping("update")
  @ApiOperation(value="更新主任务")
  public R<?> update(TaskMainEntity entity) {
    if(entity != null){
      /*清空不能被更新的字段*/
      entity.setTaskStatus(null);
      entity.setStatus(null);
      entity.setReleaseTime(null);
    }
    if(StringUtils.isNotBlank(entity.getInitiator())){
      UserEntity byId = userService.getById(entity.getInitiator());
      if(byId == null){
        return new R<>(R.失败,"无效的发起人ID");
      }
    }
    return new R<>(service.updateById(entity));
  }


  /**
   * 可新增或保存,需传入完整的父子任务结构,更新时缺失的子任务会被认定是删除!
   * @param entity
   * @return
   */
  @Override
  @ApiOperation(value ="新增或保存",notes = "需传入完整的父子任务结构树,更新时缺失的子任务会被认定是删除!")
  public R<?> save(@RequestBody TaskMainEntity entity) {
    /*校验*/
    if(StringUtils.isBlank(entity.getId())){
      try{
        CheckParametersUtil.getInstance()
            .put(entity.getTitle(),"title")
            .put(entity.getInitiator(),"initiator")
            .put(entity.getImportance(),"importance")
            .put(entity.getPriority(),"priority")
            .checkParameter();
      }catch (Exception e){
        return new R(R.失败,e.getMessage());
      }
    }
    if(entity.getImportance() != null && (entity.getImportance()>2 || entity.getImportance()<0)){
      return new R(R.失败,"importance 可选值[0,1,2]");
    }

    if(entity.getPriority() !=null && (entity.getPriority()>2 || entity.getPriority()<0)){
      return new R(R.失败,"priority 可选值[0,1,2]");
    }

    if(!StringUtils.isBlank(entity.getId())){
      if(service.getById(entity.getId()) == null){
        return new R(R.失败,"无效的更新ID");
      }
    }

    try {
      if(entity.getTaskSubList() != null && entity.getTaskSubList().size() != 0){
        /*子任务校验*/
        checkSubEntityList(entity.getTaskSubList(),"init");
      }
    } catch (BusinessException e) {
      return new R(R.失败,e.getMsg());
    }
    return new R<>(service.save(entity));
  }

  private R checkSubEntityList(List<TaskSubEntity> list,String currentIndex) throws BusinessException {
    for(int i=0;i<list.size();i++){
      TaskSubEntity subEntity = list.get(i);
      try{
        CheckParametersUtil.getInstance()
            .put(subEntity.getTitle(),"title")
            .put(subEntity.getExecutiveId(),"executiveId")
            .put(subEntity.getExpectCompleteTime(),"expectCompleteTime")
            .checkParameter();
      }catch (Exception e){
        throw new BusinessException(R.失败,"子任务" + currentIndex + "["+ i +"]" + e.getMessage());
      }
      /*合法性校验*/
      QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
      queryWrapper.and( q -> q.eq("status", TaskMainEntity.Status.启用).or().eq("status", TaskMainEntity.Status.禁用));
      queryWrapper.and(q -> q.eq("id",subEntity.getExecutiveId()));
      if(userService.getOne(queryWrapper) == null){
        return new R(R.失败,"子任务" + (i+1) + ",不存在的user:" + subEntity.getExecutiveId()  );
      }
      if(subEntity.getTaskSubList() != null && subEntity.getTaskSubList().size() != 0){
        if(!"init".equals(currentIndex)){
          currentIndex += "["+i+"]";
        }else{
          currentIndex = "["+i+"]";
        };
        checkSubEntityList(subEntity.getTaskSubList(),currentIndex);
      }
    }
    return null;
  }

}

