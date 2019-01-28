package com.aispread.manager.flowable.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 流程任务监控DTO
 * Created by Vim 2019/1/9 11:21
 *
 * @author Vim
 */
@Data
public class FlowTodoTask implements Serializable {

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 标题
     */
    private String title;

    /**
     * 状态(1,正常,0:挂起)
     */
    private Integer status;

    /**
     * 当前节点名称
     */
    private String nodeName;


    /**
     * 发起人名称
     */
    private String startUser;

    /**
     * 当前节点审批人
     */
    private String approvalUser;


    /**
     * 节点停留时间(需要转换)
     */
    private Long remainTime;

    /**
     * 节点创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 发起时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 业务标题
     */
    private String businessName;

    /**
     * 流程种类名称
     */
    private String categoryName;

    /**
     * 流程种类ID
     */
    private String categoryId;

    /**
     * 流程种类图标链接
     */
    private String categoryIco;


    /**
     * 流程种类主题色
     */
    private String categoryThemeColor;

    /**
     * 流程执行实例ID
     */
    private String instanceId;

    /**
     * 分发任务状态
     */
    private Integer distributeStatus;

    /**
     * 待办任务数量
     */
    private Integer count;
}
