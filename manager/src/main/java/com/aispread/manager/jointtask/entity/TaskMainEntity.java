package com.aispread.manager.jointtask.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redimybase.framework.mybatis.id.IdEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协同任务-主任务
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_task_main")
public class TaskMainEntity extends IdEntity<String> {

  private static final long serialVersionUID = 1L;

  public static class Status{
    public static final Integer 删除 = 0;
    public static final Integer 启用=1;
    public static final Integer 禁用 = 2;
  }

  public static class ImportanceStatus{
    public static final Integer 一般 = 0;
    public static final Integer 重要 = 1;
    public static final Integer 非常重要 = 2;
  }

  public static class PriorityStatus{
    public static final Integer 一般 = 0;
    public static final Integer 优先 = 1;
    public static final Integer 紧急 = 2;
  }

  public static class TaskStatus {
    public static final Integer 未发布 = 0;
    /*public static final Integer 未开始 = 1;*/
    public static final Integer 进行中 = 1;
    public static final Integer 完成 = 2;
  }

  @TableId(value = "id", type = IdType.ID_WORKER_STR)
  private String id;

  /**
   * 任务标题
   */
  @TableField("title")
  private String title;

  /**
   * 发起人ID
   */
  @TableField("initiator")
  private String initiator;

  /**
   * 描述
   */
  @TableField("remark")
  private String remark;

  /**
   * 重要度
   */
  @TableField("importance")
  private Integer importance;

  /**
   * 优先度
   */
  @TableField("priority")
  private Integer priority;

  /**
   * 任务状态
   */
  @TableField("task_status")
  private Integer taskStatus;

  /**
   * 状态(0:删除,1:启用,2:禁用)
   */
  @TableField("status")
  private Integer status;

  /**
   * 发布时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  @TableField("release_time")
  private Date releaseTime;

  /**
   * 最后完成时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  @TableField("last_date")
  private Date lastDate;

  /**
   * 创建人ID
   */
  @TableField("creator_id")
  private String creatorId;

  /**
   * 最后修改人ID
   */
  @TableField("reviser_id")
  private String reviserId;

  /**
   * 创建时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  @TableField("create_time")
  private Date createTime;

  /**
   * 最后更新时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  @TableField("update_time")
  private Date updateTime;

  @TableField(exist = false)
  private boolean release;

  @TableField(exist = false)
  private List<TaskSubEntity> taskSubList;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
