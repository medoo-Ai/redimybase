package com.aispread.manager.meetingNotice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableField;

import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author vim
 * @since 2019-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_meeting_notice")
public class MeetingNoticeEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private String id;

    /**
     * 通知标题
     */
    @TableField("title")
    private String title;

    /**
     * 会议议题
     */
    @TableField("content")
    private String content;

    /**
     * 会议室ID
     */
    @TableField("room_id")
    private String roomId;

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
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 创建人ID
     */
    @TableField("creator_id")
    private String creatorId;

    /**
     * 发布人
     */
    @TableField("creator")
    private String creator;

    /**
     * 修改人ID
     */
    @TableField("reviser_id")
    private String reviserId;

    /**
     * 修改人
     */
    @TableField("reviser")
    private String reviser;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 参会人员ID(逗号分隔)
     */
    @TableField("attend_user_id")
    private String attendUserId;

    /**
     * 状态(0:已保存,1:已发布,2:删除)
     */
    @TableField("status")
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Status {
        public static final Integer 已保存 = 0;
        public static final Integer 已发布 = 1;
        public static final Integer 删除 = 2;
    }

}
