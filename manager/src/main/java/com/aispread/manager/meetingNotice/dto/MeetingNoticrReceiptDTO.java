package com.aispread.manager.meetingNotice.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by Felix
 * Date : 2019/1/28
 * Description :
 */
@Data
public class MeetingNoticrReceiptDTO implements Serializable {

    /**
     * 会议通知ID
     */
    private String noticeId;

    /**
     * 回执ID
     */
    private String ReceiptId;

    /**
     * 参加人员ID
     */
    private String attendUserId;

    /**
     * 参加人员姓名
     */
    private String attendUserName;

    /**
     * 回执时间
     */
    private LocalDateTime receiptTime;

    /**
     * 回执状态(0.未回执,1.参加,2.不参加)
     */
    private Integer receiptType;

    /**
     * 回执原因
     */
    private String receiptReason;

    /**
     * 页数
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer pageSize;
}
