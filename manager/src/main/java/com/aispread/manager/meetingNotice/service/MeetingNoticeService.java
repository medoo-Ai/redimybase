package com.aispread.manager.meetingNotice.service;

import com.aispread.manager.meetingNotice.dto.MeetingNoticeDTO;
import com.aispread.manager.meetingNotice.entity.MeetingNoticeEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vim
 * @since 2019-01-27
 */
public interface MeetingNoticeService extends IService<MeetingNoticeEntity> {
    IPage<MeetingNoticeDTO> getReceivedNotice(IPage<MeetingNoticeDTO> page, MeetingNoticeDTO query);
}
