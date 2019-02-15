package com.aispread.manager.notice.service.impl;

import com.aispread.manager.notice.dto.NoticeDTO;
import com.aispread.manager.notice.entity.Notice;
import com.aispread.manager.notice.mapper.NoticeMapper;
import com.aispread.manager.notice.service.NoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-01-13
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Override
    public boolean save(Notice entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setReadFlag(0);
        return super.save(entity);
    }

    @Override
    public Page getNoticeList(Page<NoticeDTO> page, NoticeDTO notice) {
        return baseMapper.getNoticeList(page, notice);
    }

}
