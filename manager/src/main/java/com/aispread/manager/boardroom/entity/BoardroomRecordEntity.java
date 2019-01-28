package com.aispread.manager.boardroom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会议室预订记录
 * </p>
 *
 * @author vim
 * @since 2018-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_boardroom_record")
public class BoardroomRecordEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 会议主题
     */
    @TableField("title")
    private String title;

    /**
     * 会议室ID
     */
    @TableField("room_id")
    private String roomId;

    /**
     * 会议室名称
     */
    @TableField("room_name")
    private String roomName;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 会议内容
     */
    @TableField("content")
    private String content;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 所属时间区间（逗号隔开，例：1,2,3）
     */
    @TableField("time_section")
    private String timeSection;

    /**
     * 创建人ID
     */
    @TableField("creator_id")
    private String creatorId;

    /**
     * 创建人
     */
    @TableField("creator")
    private String creator;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 参会人员ID(逗号分隔)
     */
    @TableField("attend_user_id")
    private String attendUserId;

    /**
     * 状态(0:正常,1:删除)
     */
    @TableField("status")
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Status{
        public static final Integer 正常 = 0;
        public static final Integer 删除 = 1;
    }

    public static final List<String> timeSectionList = new ArrayList<>(
            Arrays.asList(
                    "08:00:00",
                    "08:30:00",
                    "09:00:00",
                    "09:30:00",
                    "10:00:00",
                    "10:30:00",
                    "11:00:00",
                    "11:30:00",
                    "12:00:00",
                    "12:30:00",
                    "13:00:00",
                    "13:30:00",
                    "14:00:00",
                    "14:30:00",
                    "15:00:00",
                    "15:30:00",
                    "16:00:00",
                    "16:30:00",
                    "17:00:00",
                    "17:30:00",
                    "18:00:00",
                    "18:30:00",
                    "19:00:00"
            )
    );

}
