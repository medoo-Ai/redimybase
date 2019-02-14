package com.aispread.manager.meetingNotice.service.impl;

import com.aispread.manager.meetingNotice.dto.MeetingNoticrReceiptDTO;
import com.aispread.manager.meetingNotice.entity.MeetingReceiptEntity;
import com.aispread.manager.meetingNotice.mapper.MeetingReceiptMapper;
import com.aispread.manager.meetingNotice.service.MeetingReceiptService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-01-28
 */
@Service
public class MeetingReceiptServiceImpl extends ServiceImpl<MeetingReceiptMapper, MeetingReceiptEntity> implements MeetingReceiptService {

    @Override
    public Page getMeetingReceiptList(Page<MeetingNoticrReceiptDTO> page, MeetingNoticrReceiptDTO query) {
        return baseMapper.getMeetingReceiptList(page, query);
    }
}
