package com.aispread.manager.announcement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class AnnouncementQueryDTO {

  /**
   * 页数
   */
  @ApiModelProperty(value = "页数")
  private Integer P_NO;

  /**
   * 数据大小
   */
  @ApiModelProperty(value = "每页数据量")
  private Integer P_SIZE;

  /**
   * 排序字段
   */
  @ApiModelProperty(value = "排序字段")
  private String orderBy;

  /**
   * 是否顺序排序
   */
  @ApiModelProperty(value = "是否顺序排序")
  private boolean asc;

  /**
   * 公告标题
   */
  @ApiModelProperty(value = "公告标题")
  private String title;

  /**
   * 发布模块(0:公司公告/1:首页图片)
   */
  @ApiModelProperty(value = "发布模块")
  private String model;

  /**
   * 发布人
   */
  @ApiModelProperty(value = "发布人ID")
  private String releaseUser;

  /**
   * 发布人名称
   */
  @ApiModelProperty(value = "发布人名称")
  private String releaseUserName;

  /**
   * 是否置顶
   */
  @ApiModelProperty(value = "是否置顶")
  private String istop;

  /**
   * 发布时间-开始时间
   */
  @ApiModelProperty(value = "发布时间-开始时间")
  private String releaseTimeStart;

  /**
   * 发布时间-结束时间
   */
  @ApiModelProperty(value = "发布时间-结束时间")
  private String releaseTimeEnd;


  /**
   * 状态(0:删除,1:草稿-默认,2:已发布)
   */
  @ApiModelProperty(value = "1:审核中(默认),2:已发布,3:已驳回,4:已下架")
  private Integer status;

}
