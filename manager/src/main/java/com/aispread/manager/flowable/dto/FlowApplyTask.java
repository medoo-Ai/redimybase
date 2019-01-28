package com.aispread.manager.flowable.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;


import java.util.Date;

/**
 * 已申请任务
 * <p>
 * Created by Vim 2019/1/11 20:21
 *
 * @author Vim
 */
@Data
public class FlowApplyTask implements Serializable {

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 业务ID
     */
    private String businessId;
    /**
     * 种类名称
     */
    private String categoryName;

    /**
     * 流程种类图标链接
     */
    private String categoryIco;


    /**
     * 流程种类主题色
     */
    private String categoryThemeColor;

    /**
     * 流程标题
     */
    private String title;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 当前节点名称
     */
    private String nodeName;

    /**
     * 审批人
     */
    private String approvalUser;

    /**
     * 发起人
     */
    private String startUser;

    /**
     * 标题
     */
    private String businessName;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 流程执行实例ID
     */
    private String instanceId;


    public static class Status {
        public static final Integer 已通过 = 1;
        public static final Integer 未通过 = 3;
        public static final Integer 已撤回 = 2;
        public static final Integer 审批中 = 4;
    }
}
