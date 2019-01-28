package com.redimybase.manager.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redimybase.framework.mybatis.id.IdEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 协同任务-子任务
 * </p>
 *
 * @author vim
 * @since 2019-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_task_sub")
public class TaskSubEntity extends IdEntity<String> {

  private static final long serialVersionUID = 1L;
  /*
    需求变更,子任务不区分状态
    public static class TaskStatus {
    public static final Integer 未开始 = 0;
    public static final Integer 进行中 = 1;
    public static final Integer 完成 = 2;
  }*/

  public static class Status{
    public static final Integer 删除 = 0;
    public static final Integer 启用=1;
    public static final Integer 禁用 = 2;
  }


  @TableId(value = "id", type = IdType.ID_WORKER_STR)
  private String id;

  /**
   * 父级任务ID
   */
  @TableField("parent")
  private String parent;

  /**
   * 任务标题
   */
  @TableField("title")
  private String title;

  /**
   * 执行人ID
   */
  @TableField("executiveId")
  private String executiveId;

  /**
   * 期望完成时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  @TableField("expect_complete_time")
  private Date expectCompleteTime;

  /**
   * 任务状态(0:未开始,1:进行中,2:完成)
   */
  /*@TableField("task_status")
  private Integer taskStatus;*/

  /**
   * 任务完成描述
   */
  @TableField("complete_desc")
  private String completeDesc;

  /**
   * 任务描述
   */
  @TableField("content")
  private String content;

  /**
   * 状态(0:删除,1:启用,2:禁用)
   */
  @TableField("status")
  private Integer status;

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
  private List<TaskSubEntity> taskSubList;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
