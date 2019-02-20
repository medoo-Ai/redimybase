package com.aispread.manager.meetingNotice.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MeetingNoticeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 会议议题
     */
    private String content;

    /**
     * 会议室ID
     */
    private String roomId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人ID
     */
    private String creatorId;

    /**
     * 发布人
     */
    private String creator;

    /**
     * 修改人ID
     */
    private String reviserId;

    /**
     * 修改人
     */
    private String reviser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 参会人员ID(逗号分隔)
     */
    private String attendUserId;

    /**
     * 状态(0:已保存,1:已发布,2:删除)
     */
    private Integer status;

    /**
     * 反馈状态（0:未反馈,1:已反馈）
     */
    private Integer receoptStatus;

    private Integer page;

    private Integer pageSize;

}
