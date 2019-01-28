package com.aispread.manager.boardroom.dto;

import com.aispread.manager.boardroom.entity.BoardroomEntity;
import com.aispread.manager.boardroom.entity.BoardroomRecordEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Felix
 * Date : 2019/1/16
 * Description :
 */
@Data
public class BoardroomRecordStatusDTO extends BoardroomEntity implements Serializable {

//    private List<BoardroomRecordEntity> recordEntityList;
    private String timeSection;
}
