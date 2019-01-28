package com.redimybase.manager.security.entity.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 子任务查询DTO
 */
@Data
public class TaskMainQuery extends TaskMainDTO {

  /**
   * 页数
   */
  private Integer P_NO;

  /**
   * 数据大小
   */
  private Integer P_SIZE;

  /**
   * 排序字段
   */
  private String orderBy;

  /**
   * 是否顺序排序
   */
  private boolean asc;
}
