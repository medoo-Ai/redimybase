package com.aispread.admin.controller.meetingNotice;


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
            entity.setStatus(MeetingNoticeEntity.Status.已保存);
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
        String users = entity.getAttendUserId();
        //判断有参会人员
        if (StringUtils.isNotBlank(users)) {
            List<String> userList = Arrays.asList(users.split(","));
            List<MeetingReceiptEntity> receiptEntityList = new ArrayList<>();
            for (String userID : userList) {
                MeetingReceiptEntity meetingReceiptEntity = new MeetingReceiptEntity();
                meetingReceiptEntity.setNoticeId(entity.getId());
                meetingReceiptEntity.setAttendUserId(userID);
                meetingReceiptEntity.setReceiptType(MeetingReceiptEntity.ReceiptType.未回执);
                receiptEntityList.add(meetingReceiptEntity);
            }
            //批量插入回执表
            receiptService.saveBatch(receiptEntityList);
        }
        return super.save(entity);
    }

    @PostMapping("update")
    @ApiOperation("修改会议通知")
    public R<?> update(MeetingNoticeEntity entity) {
        this.beforeSave(entity);
        if (service.updateById(entity)) {
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
    @ApiOperation("获取会议通知列表")
    public Object query(HttpServletRequest request) {
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
        queryWrapper.ne("status", MeetingNoticeEntity.Status.删除);
//        queryWrapper.eq("status","");
//        queryWrapper.like("content", "");
//        queryWrapper.ge("start_time", "" + " 00:00:00");
//        queryWrapper.le("end_time", "" + " 23:59:59");

        model.setData(getService().page(page, buildPageWrapper(queryWrapper, getQueryKey(request), getQuerySearch(request))));

        return model;
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

