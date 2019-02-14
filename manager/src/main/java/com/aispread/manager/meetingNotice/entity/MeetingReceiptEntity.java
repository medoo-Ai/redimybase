package com.aispread.manager.meetingNotice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author vim
 * @since 2019-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_meeting_receipt")
public class MeetingReceiptEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 会议通知ID
     */
    @TableField("notice_id")
    private String noticeId;

    /**
     * 参加人员ID
     */
    @TableField("attend_user_id")
    private String attendUserId;

    /**
     * 回执时间
     */
    @TableField("receipt_time")
    private Date receiptTime;

    /**
     * 回执状态(0.未回执,1.参加,2.不参加)
     */
    @TableField("receipt_type")
    private Integer receiptType;

    /**
     * 回执原因
     */
    @TableField("receipt_reason")
    private String receiptReason;

    public static class ReceiptType{
        public static final Integer 未回执 = 0;
        public static final Integer 参加 = 1;
        public static final Integer 不参加 = 2;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
