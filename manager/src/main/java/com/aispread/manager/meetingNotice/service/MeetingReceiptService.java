package com.aispread.manager.meetingNotice.service;

import com.aispread.manager.meetingNotice.dto.MeetingNoticeReceiptDTO;
import com.aispread.manager.meetingNotice.entity.MeetingReceiptEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vim
 * @since 2019-01-28
 */
public interface MeetingReceiptService extends IService<MeetingReceiptEntity> {
    Page getMeetingReceiptList(Page<MeetingNoticeReceiptDTO> page, MeetingNoticeReceiptDTO query);
}
