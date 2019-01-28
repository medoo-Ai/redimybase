package com.aispread.manager.flowable.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;


import java.util.Date;

/**
 * Created by Vim 2019/1/10 17:15
 *
 * @author Vim
 */
@Data
public class FlowDoneTask implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 审批节点名称
     */
    private String nodeName;

    /**
     * 发起人
     */
    private String startUser;


    /**
     * 节点创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date nodeCreateTime;

    /**
     * 种类ID
     */
    private String categoryId;

    /**
     * 流程种类名称
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
     * 业务ID
     */
    private String businessId;

    /**
     * 任务ID
     */
    private String taskId;


    /**
     * 业务标题
     */
    private String businessName;

    /**
     * 流程执行实例ID
     */
    private String instanceId;

    /**
     * 已办任务数量
     */
    private Integer count;

    public static class Status {
        public static final Integer 已通过 = 1;
        public static final Integer 未通过 = 3;
        public static final Integer 已撤回 = 2;
        public static final Integer 审批中 = 4;
    }
}
