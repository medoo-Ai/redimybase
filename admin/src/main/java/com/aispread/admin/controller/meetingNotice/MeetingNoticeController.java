package com.aispread.admin.controller.meetingNotice;


import com.aispread.manager.meetingNotice.dto.MeetingNoticeDTO;
import com.aispread.manager.meetingNotice.entity.MeetingNoticeEntity;
import com.aispread.manager.meetingNotice.entity.MeetingReceiptEntity;
import com.aispread.manager.meetingNotice.mapper.MeetingNoticeMapper;
import com.aispread.manager.meetingNotice.service.impl.MeetingNoticeServiceImpl;
import com.aispread.manager.meetingNotice.service.impl.MeetingReceiptServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author vim
 * @since 2019-01-27
 */
@RestController
@Api(tags = "会议通知管理接口")
@RequestMapping("/meetingNotice")
public class MeetingNoticeController extends TableController<String, MeetingNoticeEntity, MeetingNoticeMapper, MeetingNoticeServiceImpl> {

    @Override
    public void beforeSave(MeetingNoticeEntity entity) {
        UserEntity currentUser = SecurityUtil.getCurrentUser();
        if (null == currentUser) {
            throw new BusinessException(R.失败, "用户凭证过期,请尝试重新登录");
        }

        if (StringUtils.isBlank(entity.getId())) {
            //新增
            entity.setId(IdWorker.getIdStr());
            entity.setCreateTime(new Date());
            entity.setCreatorId(currentUser.getId());
            entity.setCreator(currentUser.getUserName());
//            entity.setStatus(MeetingNoticeEntity.Status.已保存);
        } else {
            //修改
            entity.setUpdateTime(new Date());
            entity.setReviserId(currentUser.getId());
            entity.setReviser(currentUser.getUserName());
        }
    }

    @Override
    public void afterSave(MeetingNoticeEntity entity) {
        super.afterSave(entity);
    }

    @Override
    @ApiOperation("新建会议通知")
    public R<?> save(MeetingNoticeEntity entity) {
        this.beforeSave(entity);
        if(service.save(entity)) {
            releaseNotice(entity.getId(), entity.getStatus(), entity.getAttendUserId());
            return R.ok();
        }else {
            return R.fail();
        }
    }

    @PostMapping("update")
    @ApiOperation("修改会议通知")
    public R<?> update(MeetingNoticeEntity entity) {
        this.beforeSave(entity);
        if (service.updateById(entity)) {
            releaseNotice(entity.getId(), entity.getStatus(), entity.getAttendUserId());
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Override
    @ApiOperation("删除会议通知")
    public R<?> delete(String s) {
        MeetingNoticeEntity entity = new MeetingNoticeEntity();
        entity.setId(s);
        this.beforeSave(entity);
        entity.setStatus(MeetingNoticeEntity.Status.删除);
        if (service.updateById(entity)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Override
    @ApiOperation("查询收到的会议通知")
    public Object query(HttpServletRequest request) {
        UserEntity currentUser = SecurityUtil.getCurrentUser();
        if (null == currentUser) {
            throw new BusinessException(R.失败, "用户凭证过期,请尝试重新登录");
        }

        TableModel<MeetingNoticeEntity> model = new TableModel<>();
        Page<MeetingNoticeEntity> page = (Page<MeetingNoticeEntity>) buildPageRequest(request);
        if (page == null) {
            page = new Page<>(1, 10);
        }
        QueryWrapper<MeetingNoticeEntity> queryWrapper = buildWrapper(getQueryColumn(request), getQueryValue(request));
        if (null == queryWrapper) {
            queryWrapper = new QueryWrapper<>();
        }
        queryWrapper.select("id", "title", "start_time", "end_time", "status", "creator_id", "creator", "create_time");
        queryWrapper.like("attend_user_id", currentUser.getId());

        model.setData(getService().page(page, buildPageWrapper(queryWrapper, getQueryKey(request), getQuerySearch(request))));

        return model;
    }

    @PostMapping("queryReceivedNotice")
    @ApiOperation("查询收到的会议通知列表")
    public R<?> queryReceivedNotice(MeetingNoticeDTO query){
        UserEntity currentUser = SecurityUtil.getCurrentUser();
        if (null == currentUser) {
            throw new BusinessException(R.失败, "用户凭证过期,请尝试重新登录");
        }
        query.setAttendUserId(currentUser.getId());
        Page<MeetingNoticeDTO> page = new Page<>(query.getPage(), query.getPageSize());
        return new R(service.getReceivedNotice(page, query));
    }

    @PostMapping("querySendNotice")
    @ApiOperation("获取发出的会议通知列表")
    public Object querySendNotice(HttpServletRequest request){
        UserEntity currentUser = SecurityUtil.getCurrentUser();
        if (null == currentUser) {
            throw new BusinessException(R.失败, "用户凭证过期,请尝试重新登录");
        }

        TableModel<MeetingNoticeEntity> model = new TableModel<>();
        Page<MeetingNoticeEntity> page = (Page<MeetingNoticeEntity>) buildPageRequest(request);
        if (page == null) {
            page = new Page<>(1, 10);
        }
        QueryWrapper<MeetingNoticeEntity> queryWrapper = buildWrapper(getQueryColumn(request), getQueryValue(request));
        if (null == queryWrapper) {
            queryWrapper = new QueryWrapper<>();
        }
        queryWrapper.select("id", "title", "start_time", "end_time", "status", "creator_id", "creator", "create_time");
        queryWrapper.eq("creator_id", currentUser.getId());

        model.setData(getService().page(page, buildPageWrapper(queryWrapper, getQueryKey(request), getQuerySearch(request))));

        return model;

    }

    private void releaseNotice(String noticeID, Integer status, String users){
        if(status == MeetingNoticeEntity.Status.已发布){
            //判断有参会人员
            if (StringUtils.isNotBlank(users)) {
                List<String> userList = Arrays.asList(users.split(","));
                for (String userID : userList) {
                    MeetingReceiptEntity meetingReceiptEntity = new MeetingReceiptEntity();
                    meetingReceiptEntity.setNoticeId(noticeID);
                    meetingReceiptEntity.setAttendUserId(userID);
                    meetingReceiptEntity.setReceiptType(MeetingReceiptEntity.ReceiptType.未回执);
                    //插入回执表
                    receiptService.save(meetingReceiptEntity);
                }
            }
        }
    }

    @Autowired
    private MeetingReceiptServiceImpl receiptService;

    @Autowired
    private MeetingNoticeServiceImpl service;

    @Override
    protected MeetingNoticeServiceImpl getService() {
        return service;
    }
}

