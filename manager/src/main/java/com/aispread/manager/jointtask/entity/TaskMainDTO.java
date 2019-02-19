package com.aispread.manager.jointtask.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 子任务DTO
 */
@Data
public class TaskMainDTO implements Serializable {

  private String id;

  /**
   * 是否已读:子任务有未读时为true;
   */
  private Boolean read = true;

  /**
   * 任务标题
   */
  private String title;

  /**
   * 发起人ID
   */
  private String initiator;

  /**
   * 发起人name
   */
  private String initiatorName;

  /**
   * 描述
   */
  private String remark;

  /**
   * 重要度
   */
  private Integer importance;

  /**
   * 优先度
   */
  private Integer priority;

  /**
   * 任务状态
   */
  private Integer taskStatus;

  /**
   * 发布时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  private Date releaseTime;

  /**
   * 创建人ID
   */
  private String creatorId;

  /**
   * 创建人NAME
   */
  private String creatorName;

  /**
   * 最后修改人ID
   */
  private String reviserId;

  /**
   * 最后修改人NAME
   */
  private String reviserName;

  /**
   * 创建时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  private Date createTime;

  /**
   * 最后更新时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  private Date updateTime;

  /**
   * 最后完成时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  private Date lastDate;


  /**
   * 子任务列表
   */
  private List<TaskSubDTO> taskSubList;
}
