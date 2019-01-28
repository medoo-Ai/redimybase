package com.aispread.manager.boardroom.service;

import com.aispread.manager.boardroom.entity.BoardroomRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会议室预订记录 服务类
 * </p>
 *
 * @author vim
 * @since 2018-12-28
 */
public interface BoardroomRecordService extends IService<BoardroomRecordEntity> {
    /**
     * 验证预定是否有冲突
     * @param entity 预定信息对象
     * @return 未冲突：true，冲突：false
     */
    Boolean reserveValidate(BoardroomRecordEntity entity);
}
