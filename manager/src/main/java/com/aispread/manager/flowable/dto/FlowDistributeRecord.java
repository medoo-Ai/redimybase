package com.aispread.manager.flowable.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 分发任务记录
 * Created by Vim 2019/1/18 16:47
 *
 * @author Vim
 */
@Data
public class FlowDistributeRecord implements Serializable {

    /**
     * 分发任务ID
     */
    private String id;
    /**
     * 签收人
     */
    private String assigneeName;

    /**
     * 分发时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 附件ID
     */
    private String attachmentId;

    /**
     * 内容
     */
    private String content;
}
