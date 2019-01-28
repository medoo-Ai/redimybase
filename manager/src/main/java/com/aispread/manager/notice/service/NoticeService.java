package com.aispread.manager.notice.service;

import com.aispread.manager.notice.dto.NoticeDTO;
import com.aispread.manager.notice.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vim
 * @since 2019-01-13
 */
public interface NoticeService extends IService<Notice> {
    Page getNoticeList(Page<NoticeDTO> page, NoticeDTO notice);
}
