package com.redimybase.manager.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.redimybase.framework.mybatis.id.IdEntity;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *  子任务阅读状态
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_task_read_status")
public class TaskReadStatus extends IdEntity<String> {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.ID_WORKER_STR)
  private String id;

  /**
   * 子任务ID
   */
  @TableField("tasksub_id")
  private String tasksubId;

  /**
   * 用户ID
   */
  @TableField("user_id")
  private String userId;

  /**
   * 子任务更新后是否已读
   */
  @TableField("read_status")
  private boolean readStatu;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
