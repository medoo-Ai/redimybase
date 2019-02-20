package com.aispread.admin.controller.boardroom;

import com.aispread.manager.boardroom.dto.BoardroomRecordStatusDTO;
import com.aispread.manager.boardroom.entity.BoardroomEntity;
import com.aispread.manager.boardroom.entity.BoardroomRecordEntity;
import com.aispread.manager.boardroom.mapper.BoardroomRecordMapper;
import com.aispread.manager.boardroom.service.BoardroomService;
import com.aispread.manager.boardroom.service.impl.BoardroomRecordServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.common.util.DateUtil;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;
import java.util.List;

/**
 * 会议室预订记录Controller
 * Created by Vim 2018/12/28 13:29
 *
 * @author Vim
 */
@RestController
@RequestMapping("boardroom/record")
@Api(tags = "会议室预定接口")
public class BoardroomRecordController extends TableController<String, BoardroomRecordEntity, BoardroomRecordMapper, BoardroomRecordServiceImpl> {

    @Autowired
    private BoardroomService boardroomService;

    @Override
    public void beforeSave(BoardroomRecordEntity entity) {
        UserEntity currentUser = SecurityUtil.getCurrentUser();
        if (null == currentUser) {
            throw new BusinessException(R.失败, "用户凭证过期,请尝试重新登录");
        }
        if (StringUtils.isBlank(entity.getId())) {
            //新增
            entity.setStatus(BoardroomRecordEntity.Status.正常);
            entity.setCreateTime(new Date());
            entity.setCreator(currentUser.getUserName());
            entity.setCreatorId(currentUser.getId());
        }
    }

    @Override
    public void afterSave(BoardroomRecordEntity entity) {
        //TODO 预订会议室成功后通知所有参会人员

    }

    /**
     * 预定会议室
     *
     * @param entity 预定信息
     * @return
     */
    @PostMapping("reserveBoardRoom")
    @ApiOperation("预定会议室")
    public R<?> reserveBoardRoom(BoardroomRecordEntity entity) {
        BoardroomEntity boardroomEntity = boardroomService.getById(entity.getRoomId());
        if(boardroomEntity == null){
            throw new BusinessException(R.失败, "会议室不存在");
        }
        entity.setRoomName(boardroomEntity.getName());
        if (!service.reserveValidate(entity)) {
            throw new BusinessException(R.失败, "会议室安排时间有冲突");
        }
        Boolean setFlag = false;
        String format = "HH:mm:ss";
        String startTimeStr = DateUtil.formatDate(entity.getStartTime(), format);
        String endTimeStr = DateUtil.formatDate(entity.getEndTime(), format);
        //循环static时间区间
        for (int i = 0; i < BoardroomRecordEntity.timeSectionList.size(); i++) {
            //当开始时间等于区间值时，赋值flag=true
            if (BoardroomRecordEntity.timeSectionList.get(i).equals(startTimeStr)) {
                //当结束时间等于区间值时，赋值flag=false
                if (BoardroomRecordEntity.timeSectionList.get(i).equals(endTimeStr)) {
                    setFlag = false;
                    break;
                }
                //将循环下一个判断值赋值给开始时间，以便能进入下一次赋值
                startTimeStr = BoardroomRecordEntity.timeSectionList.get(i + 1);
                setFlag = true;
            }
            //当在开始时间和结束时间区间内时，将区间值拼接给对象区间属性
            if (setFlag) {
                int tempIndex = i + 1;
                if(StringUtils.isEmpty(entity.getTimeSection())){
                    entity.setTimeSection(tempIndex + ",");
                } else {
                    entity.setTimeSection(entity.getTimeSection() + tempIndex + ",");
                }
            }
        }
        entity.setTimeSection(entity.getTimeSection().substring(0, entity.getTimeSection().lastIndexOf(",")));

        this.beforeSave(entity);
        if (service.save(entity)) {
            this.afterSave(entity);
            return R.ok();
        } else {
            return R.fail();
        }
    }

    /**
     * 获取会议室预定情况
     *
     * @param recordDate 查询的日期（yyyy-MM-dd）
     * @return
     */
    @PostMapping("getReserveBoardRoomStatus")
    @ApiOperation("获取会议室预定情况")
    public R<?> getReserveBoardRoomStatus(String recordDate) {
        if(StringUtils.isBlank(recordDate)){
            throw new BusinessException(R.失败, "未输入预定日期");
        }
        List<BoardroomRecordStatusDTO> list = new ArrayList<>();

        //获取会议室列表
        List<BoardroomEntity> boardroomList = boardroomService.getBoardroomList();
        for (BoardroomEntity boardroom : boardroomList) {
            BoardroomRecordStatusDTO dto = new BoardroomRecordStatusDTO();
            dto.setId(boardroom.getId());
            dto.setName(boardroom.getName());

            //获取当前日期下的预定记录
            QueryWrapper<BoardroomRecordEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("time_section");
            queryWrapper.eq("status", BoardroomRecordEntity.Status.正常);
            queryWrapper.eq("room_id", boardroom.getId());
            queryWrapper.ge("start_time", recordDate + " 00:00:00");
            queryWrapper.le("end_time", recordDate + " 23:59:59");
            queryWrapper.orderByAsc("start_time");
            List<BoardroomRecordEntity> recordEntityList = service.list(queryWrapper);
            for (BoardroomRecordEntity recordEntity: recordEntityList) {
                //拼接预定记录的区间
                if(StringUtils.isEmpty(dto.getTimeSection())){
                    dto.setTimeSection(recordEntity.getTimeSection());
                } else {
                    dto.setTimeSection(dto.getTimeSection() + recordEntity.getTimeSection());
                }
                dto.setTimeSection(dto.getTimeSection()+ ",");
            }
            if(!StringUtils.isEmpty(dto.getTimeSection())){
                dto.setTimeSection(dto.getTimeSection().substring(0, dto.getTimeSection().lastIndexOf(",")));
            }
//            dto.setRecordEntityList(recordEntityList);
            list.add(dto);
        }
        return new R(list);
    }

    @Override
    @ApiOperation("取消预定会议室")
    public R<?> delete(String recordID) {
        BoardroomRecordEntity entity = new BoardroomRecordEntity();
        entity.setId(recordID);
        entity.setStatus(BoardroomRecordEntity.Status.删除);
        if (service.updateById(entity)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Autowired
    private BoardroomRecordServiceImpl service;

    @Override
    protected BoardroomRecordServiceImpl getService() {
        return service;
    }

}
