package com.aispread.manager.notice.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhanghc
 * @since 2019-01-13
 */
@Data
public class NoticeDTO implements Serializable {

    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户Name
     */
    private String userName;

    /**
     * 消息标题
     */
    private String messageTitle;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 状态(0:未读,1:已读)
     */
    private Integer readFlag;

    /**
     * 创建者ID
     */
    private String createUser;

    /**
     * 创建者Name
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 页数
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer pageSize;


}
