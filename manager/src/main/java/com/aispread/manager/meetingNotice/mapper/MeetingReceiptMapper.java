package com.aispread.manager.meetingNotice.mapper;

import com.aispread.manager.meetingNotice.dto.MeetingNoticrReceiptDTO;
import com.aispread.manager.meetingNotice.entity.MeetingReceiptEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2019-01-28
 */
public interface MeetingReceiptMapper extends BaseMapper<MeetingReceiptEntity> {
    Page getMeetingReceiptList(Page<MeetingNoticrReceiptDTO> page, @Param("query") MeetingNoticrReceiptDTO query);
}
