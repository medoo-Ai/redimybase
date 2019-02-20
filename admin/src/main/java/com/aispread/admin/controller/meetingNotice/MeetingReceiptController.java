package com.aispread.admin.controller.meetingNotice;


import com.aispread.manager.meetingNotice.dto.MeetingNoticeReceiptDTO;
import com.aispread.manager.meetingNotice.entity.MeetingReceiptEntity;
import com.aispread.manager.meetingNotice.mapper.MeetingReceiptMapper;
import com.aispread.manager.meetingNotice.service.impl.MeetingReceiptServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author vim
 * @since 2019-01-28
 */
@RestController
@Api(tags = "会议通知回执管理接口")
@RequestMapping("/meetingReceipt")
public class MeetingReceiptController extends TableController<String, MeetingReceiptEntity, MeetingReceiptMapper, MeetingReceiptServiceImpl> {

    @PostMapping("getReceiptList")
    @ApiOperation("获取回执记录列表")
    public R<?> getReceiptList(MeetingNoticeReceiptDTO query) {
        Page<MeetingNoticeReceiptDTO> page = new Page<>();
        page.setCurrent(query.getPage());
        page.setSize(query.getPageSize());
        return new R(service.getMeetingReceiptList(page, query));
    }


    @PostMapping("update")
    @ApiOperation("修改回执信息")
    public R<?> update(MeetingReceiptEntity entity) {
        entity.setReceiptTime(new Date());
        if(entity.getReceiptType() == MeetingReceiptEntity.ReceiptType.不参加 && StringUtils.isBlank(entity.getReceiptReason())){
            throw new BusinessException(R.失败, "不参加必须填写原因");
        }
        if (service.updateById(entity)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @PostMapping("getReceiptInfo")
    @ApiOperation("根据消息ID和用户ID查询单条回执详情")
    public R<?> getReceiptInfo(String noticeID, String userID){
        QueryWrapper<MeetingReceiptEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notice_id", noticeID);
        queryWrapper.like("attend_user_id", userID);
        MeetingReceiptEntity entity = service.getOne(queryWrapper);
        return new R(entity);
    }

    @Autowired
    private MeetingReceiptServiceImpl service;

    @Override
    protected MeetingReceiptServiceImpl getService() {
        return service;
    }
}

