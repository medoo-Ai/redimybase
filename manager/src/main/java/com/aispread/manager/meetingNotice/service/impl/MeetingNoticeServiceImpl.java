package com.aispread.manager.meetingNotice.service.impl;

import com.aispread.manager.meetingNotice.dto.MeetingNoticeDTO;
import com.aispread.manager.meetingNotice.entity.MeetingNoticeEntity;
import com.aispread.manager.meetingNotice.mapper.MeetingNoticeMapper;
import com.aispread.manager.meetingNotice.service.MeetingNoticeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-01-27
 */
@Service
public class MeetingNoticeServiceImpl extends ServiceImpl<MeetingNoticeMapper, MeetingNoticeEntity> implements MeetingNoticeService {
    @Override
    public IPage<MeetingNoticeDTO> getReceivedNotice(IPage<MeetingNoticeDTO> page, MeetingNoticeDTO query) {
        return baseMapper.getReceivedNotice(page, query);
    }
}
