package com.redimybase.manager.security.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 子任务DTO
 */
@Data
public class TaskSubDTO {

  private String id;

  /**
   * 有修改后是否已读
   */
  private Boolean read = true;

  /**
   * 父级任务ID
   */
  private String parent;

  /**
   * 任务标题
   */
  private String title;

  /**
   * 执行人ID
   */
  private String executiveId;

  /**
   * 执行人名称
   */
  private String executiveName;

  /**
   * 期望完成时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  private Date expectCompleteTime;

  /**
   * 完成情况描述
   */
  private String completeDesc;


  /**
   * 任务描述
   */
  private String content;

  /**
   * 创建人ID
   */
  private String creatorId;

  /**
   * 创建人
   */
  private String creatorName;

  /**
   * 最后修改人ID
   */
  private String reviserId;

  /**
   * 最后修改人
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
   * 子任务列表
   */
  private List<TaskSubDTO> taskSubList;
}
