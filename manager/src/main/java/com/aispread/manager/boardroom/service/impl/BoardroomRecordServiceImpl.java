package com.aispread.manager.boardroom.service.impl;

import com.aispread.manager.boardroom.entity.BoardroomRecordEntity;
import com.aispread.manager.boardroom.mapper.BoardroomRecordMapper;
import com.aispread.manager.boardroom.service.BoardroomRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会议室预订记录 服务实现类
 * </p>
 *
 * @author vim
 * @since 2018-12-28
 */
@Service
public class BoardroomRecordServiceImpl extends ServiceImpl<BoardroomRecordMapper, BoardroomRecordEntity> implements BoardroomRecordService {

    @Override
    public Boolean reserveValidate(BoardroomRecordEntity entity) {
        Boolean flag;
        QueryWrapper<BoardroomRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id",entity.getRoomId());
        queryWrapper.ge("start_time",entity.getStartTime());
        queryWrapper.le("end_time",entity.getEndTime());
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count == 0){
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }
}
