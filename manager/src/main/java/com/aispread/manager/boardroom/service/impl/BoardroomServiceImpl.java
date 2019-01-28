package com.aispread.manager.boardroom.service.impl;

import com.aispread.manager.boardroom.entity.BoardroomEntity;
import com.aispread.manager.boardroom.mapper.BoardroomMapper;
import com.aispread.manager.boardroom.service.BoardroomService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 会议室表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2018-12-28
 */
@Service
public class BoardroomServiceImpl extends ServiceImpl<BoardroomMapper, BoardroomEntity> implements BoardroomService {
    @Override
    public List<BoardroomEntity> getBoardroomList() {
        QueryWrapper<BoardroomEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","name");
        queryWrapper.eq("status",0);
        List<BoardroomEntity> list = baseMapper.selectList(queryWrapper);
        return list;
    }
}
