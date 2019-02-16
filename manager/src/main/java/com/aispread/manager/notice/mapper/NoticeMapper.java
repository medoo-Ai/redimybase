package com.aispread.manager.notice.mapper;

import com.aispread.manager.notice.dto.NoticeDTO;
import com.aispread.manager.notice.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2019-01-13
 */
public interface NoticeMapper extends BaseMapper<Notice> {
    Page getNoticeList(Page<NoticeDTO> page, @Param("query") NoticeDTO query);
}
