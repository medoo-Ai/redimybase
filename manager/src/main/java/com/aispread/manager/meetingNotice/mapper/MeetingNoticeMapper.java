package com.aispread.manager.meetingNotice.mapper;

import com.aispread.manager.meetingNotice.dto.MeetingNoticeDTO;
import com.aispread.manager.meetingNotice.entity.MeetingNoticeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2019-01-27
 */
public interface MeetingNoticeMapper extends BaseMapper<MeetingNoticeEntity> {
    IPage<MeetingNoticeDTO> getReceivedNotice(IPage<MeetingNoticeDTO> page, MeetingNoticeDTO query);
}
