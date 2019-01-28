package com.aispread.manager.boardroom.service;

import com.aispread.manager.boardroom.entity.BoardroomEntity;
import com.aispread.manager.boardroom.entity.BoardroomRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 会议室表 服务类
 * </p>
 *
 * @author vim
 * @since 2018-12-28
 */
public interface BoardroomService extends IService<BoardroomEntity> {
    /**
     * 获取会议室列表
     * @return 会议室列表
     */
    List<BoardroomEntity> getBoardroomList();
}
